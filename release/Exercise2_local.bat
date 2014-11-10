makeblastdb -dbtype prot -in swissprot
blastp -db swissprot -query Ex1_output.fasta -out blast.out