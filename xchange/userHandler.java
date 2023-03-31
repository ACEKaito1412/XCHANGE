package com.project.xchange;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.model.Balance;
import com.project.xchange.model.Transaction;
import com.project.xchange.model.users_information;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class userHandler {
    private static DatabaseReference balance = FirebaseDatabase.getInstance().getReference("Balance");
    private static DatabaseReference ColdStore = FirebaseDatabase.getInstance().getReference("Cold_Storage");
    private static DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference("users_information");
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static final String TAG = "userHandler";
    private static String userID = user.getUid();
    private static double userBalance;
    private static String userName;
    private static String userEmail;
    private static String userType;



    private static boolean exist;

    public userHandler(){

        try{
            userInfo.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users_information userProfile = snapshot.getValue(users_information.class);

                    if(userProfile != null){
                        userName = userProfile.FName.toString() + " " + userProfile.LName.toString();
                        userEmail = userProfile.Email;
                        userType = userProfile.UserType;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: Cancelle User");
                }
            });

            balance.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Balance wallet = snapshot.getValue(Balance.class);
                    Double balance = wallet.getBalance();

                    if(wallet != null){
                        userBalance = balance;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: Cancelled balance");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getUserType() {
        return userType;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public String getUser() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public double getUserBalance() {
        if(userBalance == 0){
            return 0.0;
        }else{
            return userBalance;
        }
    }

    public String requestFaucet(int requestAmount){
        String faucet_id = "faucet-3LFXfooj5tm6ZbW";
        DatabaseReference faucetReference = FirebaseDatabase.getInstance().getReference("FAUCET");
        String transaction_id = "";
        try {
            transaction_id = String.valueOf(SystemClock.elapsedRealtime());
            if(transactionHandler("Send", faucet_id, userID, transaction_id, requestAmount)){
                faucetReference.child("CASH").setValue(ServerValue.increment(-requestAmount));
                Thread.sleep(500);
                if(transactionHandler("Receive", userID, faucet_id, transaction_id, requestAmount)){
                    updateAccountBalance(userID, requestAmount);
                    return transaction_id;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return transaction_id;
    }

    public void updateAccountBalance(String accountID, int amount){
        DatabaseReference userWallet = FirebaseDatabase.getInstance().getReference("Balance");
        try{
            userWallet.child(accountID).child("balance").setValue(ServerValue.increment(amount));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//  store transaction that happens in database
    public boolean transactionHandler(String transactionType,String senderAddress, String receiverAddress, String transactionID ,int amount){
        DatabaseReference transaction_reference = FirebaseDatabase.getInstance().getReference("Transaction");

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date dateobj = new Date();
        String date = df.format(dateobj);

//      set Transaction of the sender
        try{
            Transaction senderTransaction = new Transaction(senderAddress, receiverAddress, amount, date, transactionType);
            transaction_reference.child(senderAddress).child(transactionID).setValue(senderTransaction);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean userExist(String accountID){
        DatabaseReference userWallet = FirebaseDatabase.getInstance().getReference("Balance");
        userWallet.child(accountID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exist = snapshot.exists();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return exist;
    }

    public String createTransactioId(){
        return String.valueOf(SystemClock.elapsedRealtime());
    }



}
