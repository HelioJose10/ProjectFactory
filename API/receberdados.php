<?php
// Verifica se a requisição é um POST ou GET
if ($_SERVER['REQUEST_METHOD'] === 'POST' || $_SERVER['REQUEST_METHOD'] === 'GET') {
    // Recebe o valor enviado via POST ou GET
    $value = isset($_POST['value']) ? $_POST['value'] : $_GET['value'];

    // Conexão com o banco de dados MySQL
    $servername = "localhost";
    $username = "rubenpas_sp53";
    $password = "Password0524*";
    $dbname = "rubenpas_techelmt";

    $conn = new mysqli($servername, $username, $password, $dbname);

    // Verifica a conexão
    if ($conn->connect_error) {
        die("Falha na conexão com o banco de dados: " . $conn->connect_error);
    }

    // Obtém a hora atual
    $currentTime = date('Y-m-d H:i:s');

    // Prepara a instrução SQL para inserir o valor, hora atual e o id do utilizador na tabela
    $sql = "INSERT INTO distancia (id_utilizador, valor, hora) VALUES (3, '$value', '$currentTime')";

    if ($conn->query($sql) === TRUE) {
        echo "Valor inserido com sucesso na tabela.";
    } else {
        echo "Erro ao inserir valor na tabela: " . $conn->error;
    }

    // Fecha a conexão com o banco de dados
    $conn->close();
} else {
    echo "Método não permitido. Apenas requisições POST e GET são aceitas.";
}



?>