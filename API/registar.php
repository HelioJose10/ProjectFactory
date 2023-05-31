<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

// Verifica se todos os campos necessários estão presentes
if (!isset($_POST['nome']) || !isset($_POST['email']) || !isset($_POST['telemovel']) || !isset($_POST['password'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Missing required fields']);
    exit;
}

$host = 'localhost';
$db   = 'rubenpas_techelmt';
$user = 'rubenpas_sp53';
$pass = 'Password0524*';
$charset = 'utf8mb4';

// Cria uma conexão com o banco de dados
$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_error) {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to connect to the database']);
    exit;
}

// Obtém os dados enviados pelo formulário
$nome = $_POST['nome'];
$email = $_POST['email'];
$telemovel = $_POST['telemovel'];
$password = $_POST['password'];

// Prepara a declaração SQL e insere os dados no banco de dados
$stmt = $mysqli->prepare("INSERT INTO utilizadores (Nome, Email, Telemovel, Password) VALUES (?, ?, ?, ?)");
$stmt->bind_param("ssss", $nome, $email, $telemovel, $password);
$stmt->execute();

// Verifica se o registro foi inserido com sucesso
if ($stmt->affected_rows > 0) {
    $id = $stmt->insert_id;
    echo json_encode(['id' => $id]);
} else {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to register user']);
}

// Fecha a declaração e a conexão com o banco de dados
$stmt->close();
$mysqli->close();
?>
