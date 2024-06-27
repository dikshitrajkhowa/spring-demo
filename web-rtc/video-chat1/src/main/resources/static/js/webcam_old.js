document.addEventListener("DOMContentLoaded", function() {
    const video = document.getElementById('videoElement');
    const startButton = document.getElementById('startButton');
    const stopButton = document.getElementById('stopButton');
    const userNameInput = document.getElementById('userName');
    let stompClient = null;
    let stream = null;
    let sendVideoInterval = null;
    let userId = null;

    if (!video) {
        console.error("Video element with ID 'videoElement' not found.");
        return;
    }

    function startWebcam() {
        userId = userNameInput.value.trim() || generateUniqueId();

        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({ video: true })
                .then(function(mediaStream) {
                    stream = mediaStream;
                    video.srcObject = stream;
                    connectAndStream(stream);
                    startButton.disabled = true;
                    stopButton.disabled = false;
                })
                .catch(function(err) {
                    console.error("Error accessing the webcam: ", err);
                });
        } else {
            console.error("getUserMedia not supported by this browser.");
        }
    }

    function stopWebcam() {
        if (stream) {
            stream.getTracks().forEach(track => track.stop());
            video.srcObject = null;

            clearInterval(sendVideoInterval);

            if (stompClient) {
                stompClient.send("/app/stopStream", {}, JSON.stringify({
                    userId: userId
                }));

                stompClient.disconnect(() => {
                    console.log('Disconnected from WebSocket');
                });
            }

            startButton.disabled = false;
            stopButton.disabled = true;
        }
    }

    function connectAndStream(stream) {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
//            console.log('Connected: ', frame);
            sendVideoStream(stream);
        }, function(error) {
            console.error('WebSocket connection error: ', error);
        });
    }

    function sendVideoStream(stream) {
        const canvas = document.createElement('canvas');
        const context = canvas.getContext('2d');
        const videoWidth = video.videoWidth || 640;
        const videoHeight = video.videoHeight || 480;
        canvas.width = videoWidth;
        canvas.height = videoHeight;

        const captureInterval = 1000 / 30;

        sendVideoInterval = setInterval(function() {
            context.drawImage(video, 0, 0, videoWidth, videoHeight);
            const dataUrl = canvas.toDataURL('image/webp');
//            console.log("Sending video data: ", dataUrl.substring(0, 50), "...");

            if (stompClient && stompClient.connected) {
                stompClient.send("/app/stream", {}, JSON.stringify({
                    userId: userId,
                    streamData: dataUrl
                }));
            }
        }, captureInterval);
    }

    function generateUniqueId() {
        return 'user_' + Math.random().toString(36).substr(2, 9);
    }

    window.startWebcam = startWebcam;
    window.stopWebcam = stopWebcam;
});
