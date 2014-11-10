TP-Bioinformatica
=================

Trabajo práctico de Bioinformática - UTN FRBA - 20142C


*** Ejercicio 1 ***

Los pasos realizados están descriptos con comentarios en el código fuente, que es el archivo Ex1.java, ubicado en el directorio "src".

Para ejecutar el script, se debe acceder al directorio "release" desde la línea de comandos, escribir "java -jar Ex1.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la máquina virtual de Java, y debe estar presente el archivo input "Ex1_input.gb" en el directorio "input".


*** Ejercicio 2 ***

Los pasos realizados están descriptos con comentarios en el código fuente, que es el archivo Ex2.java, ubicado en el directorio "src".

Para ejecutar el script, se debe acceder al directorio "release" desde la línea de comandos, escribir "java -jar Ex2.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la máquina virtual de Java, y debe estar presente el archivo resultado del script anterior, "Ex1_output.fasta", en el directorio "output".

Este script ejecuta en forma remota un alineamiento, por lo que para obtener los resultados esperados es necesario también contar con acceso a Internet.


Ejercicio 2 - Blast versión local

También preparamos una versión que corre Blast localmente. Para probarla, ejecutar el archivo "Ex2_local.bat".

Precondiciones:
	- Sistema operativo Windows (no fue probado en Linux)
	- Presencia de los binarios del NCBI "makeblastdb" y "blastp" en el directorio actual (incluimos los binarios para Windows)
	- Presencia de la base de datos de proteínas descomprimida en el directorio actual. El archivo debe llamarse "swissprot". No la incluimos en la entrega por una cuestión de tamaño. La versión comprimida se descarga de ftp://ftp.ncbi.nlm.nih.gov/blast/db/FASTA/swissprot.gz



*** Ejercicio 3 ***

Para buscar un patrón ingresado por teclado en el archivo obtenido luego de la ejecución de Blast, no encontramos una función apropiada dentro de BioJava, por lo que hicimos el parseo directamente con funciones nativas que manipulan archivos y strings. Tomamos como premisa que cada uno de los resultados arrojados por Blast comienza con el caracter '>'.

Para ejecutar el script, se debe acceder a este directorio desde la línea de comandos, escribir "java -jar Ex3.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la máquina virtual de Java.

