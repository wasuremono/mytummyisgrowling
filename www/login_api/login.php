<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
#if (isset($_POST['email']) && isset($_POST['password'])) {
if (isset($_POST['email'])){
    // receiving the post params
    $email = $_POST['email'];
    $password = $_POST['password'];
    if ($db->userExists($email)){
        // get the user by email and password
        $user = $db->verifyUser($email, $password);
    
        if ($user != false) {
            // use is found
            $response["error"] = FALSE;
            $response["uid"] = $user["id"];
            $response["email"] = $user["email"];
            $response["errorMessage"] = "";
            #$response["user"]["created_at"] = $user["created_at"];
            #$response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user is not found with the credentials
            $response["error"] = TRUE;
            $response["uid"] = "0";
            $response["email"] = "";
            $response["errorMessage"] = "Password Incorrect.";
            echo json_encode($response);
        }
    } else {
        $response["error"] = TRUE;
        $response["uid"] = "0";
        $response["email"] = "";
        $response["errorMessage"] = "Specified email does not exist.";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["uid"] = "0";
    $response["email"] = "";
    $response["errorMessage"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>