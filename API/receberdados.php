<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST' || $_SERVER['REQUEST_METHOD'] === 'GET') {
    $value = isset($_POST['value']) ? $_POST['value'] : $_GET['value'];
    $id = isset($_POST['id']) ? $_POST['id'] : $_GET['id'];

    $servername = "localhost";
    $username = "rubenpas_sp53";
    $password = "Password0524*";
    $dbname = "rubenpas_techelmt";

    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_error) {
        die("Falha na conexão com o banco de dados: " . $conn->connect_error);
    }

    $checkUserQuery = "SELECT id FROM utilizadores WHERE id = '$id'";
    $result = $conn->query($checkUserQuery);
    
    if ($result->num_rows == 0) {
        echo "ID do usuário não existe. Valor não inserido.";
        exit;
    }

    $currentTime = date('Y-m-d H:i:s');

    $sql = "INSERT INTO distancia (id_utilizador, valor, hora) VALUES ('$id', '$value', '$currentTime')";

    if ($conn->query($sql) === TRUE) {
        echo "Valor inserido com sucesso na tabela.";
    } else {
        echo "Erro ao inserir valor na tabela: " . $conn->error;
    }

    $conn->close();
} else {
    echo "Método não permitido. Apenas requisições POST e GET são aceitas.";
}
?>