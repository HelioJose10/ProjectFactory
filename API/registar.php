<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

if (!isset($_GET['nome']) || !isset($_GET['email']) || !isset($_GET['telemovel']) || !isset($_GET['password'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Missing required fields']);
    exit;
}

$host = 'localhost';
$db   = 'rubenpas_techelmt';
$user = 'rubenpas_sp53';
$pass = 'Password0524*';
$charset = 'utf8mb4';

$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_error) {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to connect to the database']);
    exit;
}

$nome = $_GET['nome'];
$email = $_GET['email'];
$telemovel = $_GET['telemovel'];
$password = $_GET['password'];

$stmt = $mysqli->prepare("INSERT INTO utilizadores (Nome, Email, Telemovel, Password) VALUES (?, ?, ?, ?)");
$stmt->bind_param("ssss", $nome, $email, $telemovel, $password);
$stmt->execute();

if ($stmt->affected_rows > 0) {
    $id = $stmt->insert_id;
    echo json_encode(['id' => $id]);
} else {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to register user']);
}

$stmt->close();
$mysqli->close();
?>
