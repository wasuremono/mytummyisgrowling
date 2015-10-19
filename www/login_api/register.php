<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // check if user is already existed with the same email
    if ($db->userExists($email)) {
        // user already exists
        $response["error"] = TRUE;
        $response["id"] = "0";
        $response["error_msg"] = "The email address " . $email . " is already in use";
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($name, $email, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["id"] = $user["id"];
            $response["error_msg"] = "";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["id"] = "0";
            $response["error_msg"] = "Unable to create user";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["id"] = "0";
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    #echo json_encode($response);
}
?>