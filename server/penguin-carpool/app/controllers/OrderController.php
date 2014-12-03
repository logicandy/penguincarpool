<?php

class OrderController extends \BaseController {
	// if(Auth::check()){
	// 	$loggedinID = Auth::user()->id;
	// }
	// else{
	// 	//nothing...
	// }
	public function create(){
		$input_order = Input::all();
		//return $input_order;
		$OrderTable = new Order;
		
		$OrderTable->user_location = $input_order['User_Location'];
		$OrderTable->user_destination = $input_order['User_destination'];
		// $OrderTable->user_id = Auth::user()->id;
		$OrderTable->user_id = (int)$input_order['user_id'];

		//id 1 taxi
		$taxiTable = Taxi::find(1);
		$OrderTable->taxi_id = $taxiTable->id;

		$OrderTable->save();

		// return Response::json(['status'=>200]);
	}

	public function getLoc(){

		$id = 49;
		//or try Auth::user()->id
	
		$orderloc = DB::table('orders')->where('user_id','=', $id)->pluck('user_location');
		$orderdest = DB::table('orders')->where('user_id','=', $id)->pluck('user_destination');

		return Response::json(['status'=> 200, 'Location'=> $orderloc, 'Destination'=>$orderdest]);

	}

	public function sendMessage(){
		$input_message = Input::all();
		//return $input_message;
		// $userTable = new User;
		// return $input_message;
		$message = $input_message['Type_Message'];
		$id = $input_message['User_ID'];

		$user_find = DB::table('users')->where('id','=',$id)->pluck('first_name');
		// $user_id_int = $input_message['taxi_location'];
		// $taxi = Taxi::find($message);
		// return $user_find;

		$taxis = DB::table('taxis')->where('taxi_location', '=', $message)->pluck('user_id');

		$userTable = new User;
		$user = $userTable::find($taxis);
		//string concat
		$msg = 'User ' . $user_find . ' would like to share a taxi with you!';
		
		$user->message = $msg;

		$user->save();
	}

}