package com.example.messagelogin.customer;

import com.google.firebase.firestore.auth.User;

public interface ConversationListener {
    void onConversationClicked(UserMdl user);
}
