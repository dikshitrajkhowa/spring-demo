document.addEventListener("DOMContentLoaded", function() {
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ', frame);
        stompClient.subscribe('/topic/stream', function(message) {
            const streamData = JSON.parse(message.body);
            console.log("Received stream data: ", streamData);
            displayVideoStreams(streamData);
        });
    });

    function displayVideoStreams(data) {
        const userVideos = document.getElementById('userVideos');

        // Clear previous video elements
        userVideos.innerHTML = '';

        for (const userId in data) {
            if (data.hasOwnProperty(userId)) {
                let videoContainer = document.getElementById(userId);

                if (!videoContainer) {
                    videoContainer = document.createElement('div');
                    videoContainer.id = userId;
                    videoContainer.className = 'video-container';
                    userVideos.appendChild(videoContainer);
                }

                const img = document.createElement('img');
                img.src = data[userId];
                videoContainer.innerHTML = ''; // Clear any previous content
                videoContainer.appendChild(img);
            }
        }
    }
});
