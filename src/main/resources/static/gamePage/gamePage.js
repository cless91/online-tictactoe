var gameData;

const gameId = new URLSearchParams(window.location.search).get('gameId');
var playerId = Cookies.get('playerId');

const socket = new WebSocket("ws://localhost:8080/singleGame?gameId="+gameId);
socket.binaryType = "arraybuffer";

const startGameElem = document.getElementById("startGame");

socket.onopen = function (event) {
};

socket.onmessage = function (event) {
    var data = JSON.parse(event.data);
    var opCode = data['opCode'];

    if (opCode === 'gameUpdated') {
        gameData = data.game;
        updateGameDisplayFromGameData();
    }
};

$.post( "http://localhost:8080/gameData/"+gameId, function(data) {
    gameData = data;
    updateGameDisplayFromGameData();
})
.fail(function() {
    alert( "error getting game information data" );
});

function startGame(gameId) {
    $.post("http://localhost:8080/startGame/" + gameId, function () {
        window.location.href = '/gamePage/gamePage.html?gameId=' + gameId;
    })
        .fail(function () {
            alert("error starting game" + gameId);
        });
}

function updateGameDisplayFromGameData(){
    document.getElementById("gameTitle").innerHTML=gameData.title;
    document.getElementById("gameState").innerHTML=gameData.gameState;
    document.getElementById("player1").innerHTML=gameData.creator;
    document.getElementById("player2").innerHTML=gameData.otherPlayer;

    if(gameData.gameState === "READY" && playerId === gameData.creator){
        startGameElem.innerHTML = '<button onclick="startGame(\'' + gameData.id + '\')" type="button" class="btn btn-primary btn-sm">Start Game</button>'
    }
}