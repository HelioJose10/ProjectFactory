<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

// Verifique se o e-mail e a senha foram fornecidos
if (!isset($_GET['email']) || !isset($_GET['password'])) {
    http_response_code(400); // Bad request
    echo json_encode(['error' => 'Missing email or password']);
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
$stmt = $mysqli->prepare("SELECT id FROM Utilizadores WHERE email = ? AND password = ?");
$stmt->bind_param("ss", $_GET['email'], $_GET['password']);
$stmt->execute();

// Obter o resultado
$result = $stmt->get_result();
$user = $result->fetch_assoc();

if ($user) {
    // Retornar o ID do usuário como JSON
    echo json_encode(['id' => $user['id']]);
} else {
    http_response_code(404); // Not found
    echo json_encode(['error' => 'Invalid email or password']);
}

$stmt->close();
$mysqli->close();
?>
