-----ENGLISH-----

LZ77.java
	FUNCTIONS
	encode
	encode_text ((the only diference is that takes the text as a parameter, in binary format))
	decode
	decode_text
	saveToFile

txtReader.java
	Reads the file and changes it to a binary format, or takes a binari and changes to an ASCII format.

OBSERVATIONS
	"hamlet_short.txt": -Maximun compression value around 1.2
			    -The best parameters are SLIDER size of  4096 and ENTRY size of 64
	
	"quijote_short.txt" -Maximun compression value around 1.1
			    -The best parameters are SLIDER size of  4096 and ENTRY size of 124


	-Betters compression factors as SLIDER is bigger
	-"hamblet" gets better factor with ENTRY relatively hihgh because it allow to get the actor names, wich can be totally compressed.



-----ESPAÑOL-----
LZ77.java
	FUNCIONES
	encode
	encode_text ((la unica diferencia es que coge el texto como parametro, ya pasado a binario))
	decode
	decode_text
	saveToFile

txtReader.java
	Lee el archivo pasado por parametro y lo convierte a binario o de binario a ASCII.

OBSERVACIONES
	"hamlet_short.txt": -Alcanza valores máximos de compresión entorno al 1.2
			    -El menjor parametro es para SLIDER de tamaño 4096 y ENTRY de tamaño 64
	
	"quijote_short.txt" -Alcanza compresión en torno al 1.1 como máximo
			    -El mejor parámetro es para SLIDER 4096 y ENTRY 124


	-Se alcanzan mejores factores cuanto más alto es el SLIDER.
	-hamlet alcanza mejor factor con ENTRYs relativamente altos ya que permite llegar a los nombres de los personajes que se repiten constantemente y comprimirlos enteros.