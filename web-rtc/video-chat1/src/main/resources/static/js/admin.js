window.onbeforeunload = function (e) {
// Your logic to prepare for 'Stay on this Page' goes here

    return "Please click 'Stay on this Page' and we will give you candy";
};

document.addEventListener("DOMContentLoaded", function() {
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    const userListContainer = document.getElementById('userListContainer');
    const userVideos = document.getElementById('userVideos');

    stompClient.connect({}, function(frame) {
        console.log('Connected: ', frame);
        stompClient.subscribe('/topic/stream', function(message) {
            const streamData = JSON.parse(message.body);
            console.log("Received stream data: ", streamData);
            updateUserList(streamData);
            displayVideoStreams(streamData);
        });

        stompClient.subscribe('/topic/stopStream', function(message) {
            const stopData = JSON.parse(message.body);
            console.log("Received stop stream data: ", stopData);
            removeVideoStream(stopData.userId);
            removeUserFromList(stopData.userId);
        });
    });

    function updateUserList(data) {
        userListContainer.innerHTML = '';
        for (const userId in data) {
            if (data.hasOwnProperty(userId)) {
                let listItem = document.getElementById(`list-${userId}`);
                if (!listItem) {
                    listItem = document.createElement('li');
                    listItem.id = `list-${userId}`;
                    listItem.textContent = userId;
                    listItem.onclick = () => focusOnUser(userId);
                    userListContainer.appendChild(listItem);
                }
            }
        }
    }

    function focusOnUser(userId) {
        const videoContainer = document.getElementById(userId);
        if (videoContainer) {
            videoContainer.scrollIntoView({ behavior: 'smooth' });
            highlightUserVideo(userId);
        }
    }

    function highlightUserVideo(userId) {
        const videoContainers = document.querySelectorAll('.video-container');
        videoContainers.forEach(container => {
            container.style.border = container.id === userId ? '2px solid blue' : '2px solid #ddd';
        });
    }

    function displayVideoStreams(data) {
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
                const userName = document.createElement('h2');
                userName.textContent = userId;

                videoContainer.innerHTML = '';
                videoContainer.appendChild(userName);
                videoContainer.appendChild(img);
            }
        }
    }

    function removeVideoStream(userId) {
        const videoContainer = document.getElementById(userId);
        if (videoContainer) {
            videoContainer.remove();
            console.log(`Removed video stream for user: ${userId}`);
        }
    }

    function removeUserFromList(userId) {
        const listItem = document.getElementById(`list-${userId}`);
        if (listItem) {
            listItem.remove();
            console.log(`Removed user from list: ${userId}`);
        }
    }
});
