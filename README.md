# Practica-5

# OBJETIVO (COMPETENCIA)
Desarrollar un programa utilizando distintas clases que interactúan, haciendo uso de vectores
y arrays, con disposición para comprender el análisis y diseño en la programación orientada a
objetos.
# Introducción
En esta práctica, se utilizan vectores. Al utilizar los vectores es recomendable recordar que
éstos son objetos y que tenemos a nuestra disposición algunas clases del API que nos facilitan
su procesamiento. Finalmente, se requerirá el uso de archivos de texto, el uso de los cuales
queda como responsabilidad del estudiante.
# Desarrollo
Analiza el funcionamiento del juego Batalla Naval. El juego básicamente consiste en manejar
un tablero de océano y un tablero de tiro; cada uno divididos en casillas. Cada tablero
representa una zona diferente del mar abierto: la propia y la contraria. En el primer tablero, el
jugador coloca sus barcos y registra los «tiros» del oponente; en el otro, se registran los tiros
propios contra el otro jugador, diferenciando los impactos y los que dan al agua.
A continuación, puede consultar más información sobre este juego: Battleship (game). En el
siguiente enlace encontrará un ejemplo de gameplay de Battleship War.
Para el desarrollo de esta práctica, se requiere diseñar e implementar algunas clases que se
dedican a modelar e implementar algunas de las funciones requeridas para jugar batalla naval
en modo gráfico. Deberá dibuja y describir las clases, atributos y métodos de esta jerarquía en
su reporte.

Al comenzar la aplicación, cada jugador posicionará sus barcos en el primer tablero, de forma
secreta, invisible al oponente 2. Cada quien ocupa, según sus preferencias, una misma
cantidad de casillas, horizontal y/o verticalmente, las que representan sus naves. Ambos
participantes deben ubicar igual número de naves, por lo que es habitual, antes de comenzar,
estipular de común acuerdo la cantidad y el tamaño de las naves que se posicionarán en el
tablero.
Una vez todas las naves han sido posicionadas, se inicia una serie de rondas. En cada ronda,
cada jugador en su turno «dispara» hacia la flota de su oponente indicando una posición (las
coordenadas de una casilla), la que registra en el segundo tablero. Si esa posición es ocupada
por parte de un barco contrario, el oponente cantará ¡Impacto! si todavía quedan partes del
barco (casillas) sin dañar, o ¡Hundido! si con ese disparo la nave ha quedado totalmente
destruida (esto es, si la acertada es la última de las casillas que conforman la nave que
quedaba por acertar). El jugador que ha tocado un barco en su anterior jugada, volverá a
disparar hasta que falle. Si la posición indicada no corresponde a una parte de barco alguno,
cantará ¡Agua!.

El juego puede terminar con un ganador o en empate:
Hay ganador: quien destruya primero todas las naves de su oponente será el vencedor.
Empate: si bien lo habitual es continuar el juego hasta que haya un ganador, el empate también
puede alcanzarse si, tras haber disparado cada jugador una misma cantidad de tiros fija y
predeterminada (como una variante permitida en el juego), ambos jugadores han acertado en
igual número de casillas contrarias.
# Consideraciones
a. Toda la interacción con el usuario será mediante una GUI.
b. Se deberán utilizar vectores de manera significativa en la implementación.
c. Se deberá utilizar al menos uno de los métodos de la clase Arrays y otro de la clase
Array, excluyendo los métodos toString().
d. La manipulación de un archivo será necesaria para guardar las posiciones de ambos
jugadores (save file), de esta manera, si la aplicación se cierra, se puede reanudar el
juego en el punto donde se dejó.

# Notas Adicionales
Se deberá incluir en el reporte un diagrama de clases, la descripción de cada una y la
descripción de cómo interactúan.
Se deberá incluir en el reporte el diagrama de clases en el que se muestra la jerarquía así
como una descripción breve de cada clase.
