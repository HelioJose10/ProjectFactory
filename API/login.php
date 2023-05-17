<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

// Verifique se o ID foi fornecido
if (!isset($_GET['id'])) {
    http_response_code(400); // Bad request
    echo json_encode(['error' => 'Missing user ID']);
    exit;
}

// Conectar ao banco de dados
$host = 'localhost';
$db   = 'percursoDB';
$user = 'username'; // substitua com seu username do MySQL
$pass = 'password'; // substitua com sua password do MySQL
$charset = 'utf8mb4';

$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_error) {
    http_response_code(500); // Internal server error
    echo json_encode(['error' => 'Failed to connect to the database']);
    exit;
}

// Preparar e executar a consulta
$stmt = $mysqli->prepare("SELECT * FROM Utilizadores WHERE id = ?");
$stmt->bind_param("i", $_GET['id']);
$stmt->execute();

// Obter o resultado
$result = $stmt->get_result();
$user = $result->fetch_assoc();

if ($user) {
    // Retornar os dados do usuário como JSON
    echo json_encode($user);
} else {
    http_response_code(404); // Not found
    echo json_encode(['error' => 'User not found']);
}

$stmt->close();
$mysqli->close();
?>
