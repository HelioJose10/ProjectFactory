<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Content-Type: application/json; charset=UTF-8");

// Verificar se o parâmetro 'id' foi recebido na URL
if(isset($_GET['id'])) {
    $id = $_GET['id'];
} else {
    http_response_code(400); // Bad Request
    echo json_encode(['error' => 'Parameter \'id\' is missing']);
    exit;
}

// Conectar ao banco de dados
$host = 'localhost';
$db   = 'rubenpas_techelmt';
$user = 'rubenpas_sp53'; // substitua com seu username do MySQL
$pass = 'Password0524*'; // substitua com sua password do MySQL
$charset = 'utf8mb4';

$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_error) {
    http_response_code(500); // Internal server error
    echo json_encode(['error' => 'Failed to connect to the database']);
    exit;
}

// Definir o conjunto de caracteres para UTF-8
$mysqli->set_charset($charset);

// Preparar e executar a consulta
$stmt = $mysqli->prepare("SELECT Nome, Email, Telemovel FROM utilizadores WHERE id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();

// Obter o resultado
$result = $stmt->get_result();

// Verificar se há dados encontrados
if ($result->num_rows > 0) {
    $data = $result->fetch_assoc();
    echo json_encode($data, JSON_UNESCAPED_UNICODE);
} else {
    http_response_code(404); // Not found
    echo json_encode(['error' => 'No data found']);
}

$stmt->close();
$mysqli->close();
?>
