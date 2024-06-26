document.addEventListener("DOMContentLoaded", function() {
    const video = document.getElementById('videoElement');
    const userId = generateUniqueId(); // Function to generate a unique user ID
    let stompClient = null;

    if (!video) {
        console.error("Video element with ID 'videoElement' not found.");
        return;
    }

    function startWebcam() {
        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({ video: true })
                .then(function(stream) {
                    video.srcObject = stream;
                    connectAndStream(stream);
                })
                .catch(function(err) {
                    console.error("Error accessing the webcam: ", err);
                });
        } else {
            console.error("getUserMedia not supported by this browser.");
        }
    }

    function connectAndStream(stream) {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            console.log('Connected: ', frame);
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

        setInterval(function() {
            context.drawImage(video, 0, 0, videoWidth, videoHeight);
            const dataUrl = canvas.toDataURL('image/webp');
            console.log("Sending video data: ", dataUrl.substring(0, 50), "...");

            // Send data with userId
            stompClient.send("/app/stream", {}, JSON.stringify({
                userId: userId,
                streamData: dataUrl
            }));
        }, captureInterval);
    }

    function generateUniqueId() {
        return 'user_' + Math.random().toString(36).substr(2, 9);
    }

    window.startWebcam = startWebcam;
});
