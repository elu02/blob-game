KNOWN BUGS:
- you can get stuck inside a wall if you grow bigger beside a wall. You grow into the wall.
- the text (i.e. the score, the leaderboard) in game are kinda shaky. This is simply due to the fact that the scale is a double (necessary for the game to be able to zoom out smoothly)
while the Graphics.drawString method and Font object only takes in integers for the x and y coordinates and the size, respectively. No easy fix for this that I see.
TODO LIST:
- add bots
- add mass decay as you get bigger
- add splitting 
- add main starting screen
- add rules screen
- add about screen
- add better comments
- make blobs slow down as they get bigger
- add virus blobs

IDEAS: 
- maybe to scale bot difficulty just give main player a handicap (i.e. bots get triple value from eating food)
- make virus blobs that spit out set amounts of food 
- choose name
- make it so that bots will hunt you when they are bigger than you (maybe make this a percent chance?)
- make a danger symbol (maybe a triangle?) above player which becomes more and more red the more blobs are hunting you
- maybe make it so that bots will hunt other bots
