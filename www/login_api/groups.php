<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
if(strcmp($_POST['activity'],"create") == 0){
    $groupInfo = $db->createGroup($_POST['userId'],$_POST['groupname'],$_POST['password']);
    $group["id"] = $groupInfo["id"];
    $group["leaderId"] = $groupInfo["owner"];
    $group["name"] = $groupInfo["groupName"]; 
    $group["pass"] = $groupInfo["password"];
    $db->addMember($group["id"],$group["leaderId"]);
    $members = $db->getGroupMembers($group["id"]); 
    foreach( $members  AS $value ) {
            $memberIds[] = $value["userId"];
    }
    
    $group["memberIds"] = $memberIds;
    echo json_encode($group);
} else if(strcmp($_POST['activity'],"read") == 0){
    $result = $db->getGroups($_POST["userId"]);
    if($result != false){
        echo json_encode($result);
    }
} else if(strcmp($_POST['activity'],"join") == 0){
    if($db->joinGroup($_POST['groupId'],$_POST['password'],$_POST['userId'])){
    $result = $db->getGroups($_POST["userId"]);
    if($result != false){
        echo json_encode($result);
    }
    }
} else if(strcmp($_POST['activity'],"getName") == 0){
    $user = $db->getUserInfo($_POST["userId"]);
    $user["prefs"] = $db->getPrefs($_POST["userId"]);
    echo json_encode($user);
}
else if(strcmp($_POST['activity'],"getPrefs") == 0){
    $prefs = $db->getPrefs($_POST["userId"]);
    echo json_encode($prefs);
}
    
        
    

        
?>