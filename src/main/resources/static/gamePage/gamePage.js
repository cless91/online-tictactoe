var gameData;

const gameId = new URLSearchParams(window.location.search).get('gameId');
$.post( "http://localhost:8080/gameData/"+gameId, function(data) {
    gameData = data;
    updateGameDisplayFromGameData(data);
})
.fail(function() {
    alert( "error getting game information data" );
});

function updateGameDisplayFromGameData(data){
    document.getElementById("gameTitle").innerHTML=gameData.title;
    document.getElementById("gameState").innerHTML=gameData.gameState;
    document.getElementById("players").innerHTML+=`<li>creator: ${gameData.creator}`;
    document.getElementById("players").innerHTML+=`<li>other player: ${gameData.otherPlayer}`;
}