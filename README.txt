README
This game is Run by the GameDirector, 
which controls the flow of the game and all the matches and turns. 
The information from the Server is handled by ServerInterface,
which looks for specific words in the messages received from the user to determine what should be sent back.
The Board is stored with the class Coordinates, in an Array of BoardSpaces, which may or may not have a tile.
When the game connects to the server, all the tiles are generated based on the tiles that are given, 
and the AI uses only the board to determine which move it will make next. 
It doesn’t use any kind of scoring to do this, just the board in is present state each turn.
