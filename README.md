TP-Bioinformatica
=================

Trabajo pr�ctico de Bioinform�tica - UTN FRBA - 20142C


*** Ejercicio 1 ***

Los pasos realizados est�n descriptos con comentarios en el c�digo fuente, que es el archivo Exercise1.java, ubicado en el directorio "src".

Para ejecutar el script, se debe acceder al directorio "release" desde la l�nea de comandos, escribir "java -jar Exercise1.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la m�quina virtual de Java, versi�n 1.7, y debe estar presente el archivo input "Ex1_input.gb" en el directorio "release/data/input".


*** Ejercicio 2 ***

Los pasos realizados est�n descriptos con comentarios en el c�digo fuente, que es el archivo Exercise2.java, ubicado en el directorio "src".

Para ejecutar el script, se debe acceder al directorio "release" desde la l�nea de comandos, escribir "java -jar Exercise2.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la m�quina virtual de Java, versi�n 1.7, y debe estar presente el archivo resultado del script anterior, "Ex1_output.fasta", en el directorio "release/data/output".

Este script ejecuta en forma remota un alineamiento, por lo que para obtener los resultados esperados es necesario tambi�n contar con acceso a Internet.


Ejercicio 2 - Blast versi�n local

Tambi�n preparamos una versi�n que corre Blast localmente. Para probarla, ejecutar el archivo "Exercise2_local.bat".

Precondiciones:
	- Sistema operativo Windows (no fue probado en Linux)
	- Presencia de los binarios del NCBI "makeblastdb" y "blastp" en el directorio actual (incluimos los binarios para Windows)
	- Presencia de la base de datos de prote�nas descomprimida en el directorio actual. El archivo debe llamarse "swissprot". No la incluimos en la entrega por una cuesti�n de tama�o. La versi�n comprimida se descarga de ftp://ftp.ncbi.nlm.nih.gov/blast/db/FASTA/swissprot.gz


*** Ejercicio 3 ***

Para buscar un patr�n ingresado por teclado en el archivo obtenido luego de la ejecuci�n de Blast, no encontramos una funci�n apropiada dentro de BioJava, por lo que hicimos el parseo directamente con funciones nativas que manipulan archivos y strings. Tomamos como premisa que cada uno de los resultados arrojados por Blast comienza con el caracter '>'.

Para ejecutar el script, se debe acceder al directorio "release" desde la l�nea de comandos, escribir "java -jar Ex3.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la m�quina virtual de Java, versi�n 1.7.


*** Ejercicio 4 ***

Utilizamos los programas "getorf" y "patmatmotifs" de la suite EMBOSS para obtener los ORF y analizar los dominios respectivamente.

Para ejecutar el script, se debe acceder al directorio "release" desde la l�nea de comandos, escribir "java -jar Exercise4.jar" y presionar Enter. Para que funcione, debe estar instalada correctamente la m�quina virtual de Java, versi�n 1.7., y tambi�n EMBOSS. Adem�s, de ser necesario, habr� que generar el archivo prosite.lines con el programa "prosextract" (en Windows ya se instala junto con los binarios de EMBOSS).
