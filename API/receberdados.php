<?php
// Verifica se a requisição é um POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    // Conexão com o banco de dados MySQL
    $servername = "localhost";
    $username = "rubenpas_techelmt";
    $password = "Password0524**";
    $dbname = "rubenpas_techelmt";

    $conn = new mysqli($servername, $username, $password, $dbname);

    // Verifica a conexão
    if ($conn->connect_error) {
        die("Falha na conexão com o banco de dados: " . $conn->connect_error);
    }

    // Recebe o JSON enviado no corpo do POST
    $json = file_get_contents('php://input');
    $data = json_decode($json, true);

    // Verifica se o campo "value" está presente no JSON
    if (isset($data['value'])) {
        $value = $data['value'];

        // Prepara a instrução SQL para inserir o valor na tabela
        $sql = "INSERT INTO testes (valor) VALUES ('$value')";

        if ($conn->query($sql) === TRUE) {
            echo "Valor inserido com sucesso na tabela.";
        } else {
            echo "Erro ao inserir valor na tabela: " . $conn->error;
        }
    } else {
        echo "Campo 'value' não encontrado no JSON.";
    }

    // Fecha a conexão com o banco de dados
    $conn->close();
} else {
    echo "Método não permitido. Apenas requisições POST são aceitas.";
}
?>
