package com.example.firebasewithkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {


    private lateinit var chatRecylerView: RecyclerView
    private lateinit var chatEditText: EditText
    private lateinit var chatButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var databaseReference: DatabaseReference

    var ReceiverRoom: String? = null
    var SenderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")

        supportActionBar?.title = name


        chatRecylerView = findViewById(R.id.chatRecylerview)
        chatEditText = findViewById(R.id.messageBox)
        chatButton = findViewById(R.id.send_Btn)
        databaseReference = FirebaseDatabase.getInstance().getReference()


        val senderuid = FirebaseAuth.getInstance().currentUser?.uid
        SenderRoom = senderuid + receiveruid
        ReceiverRoom = receiveruid + senderuid



        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecylerView.layoutManager = LinearLayoutManager(this)
        chatRecylerView.adapter = messageAdapter









        databaseReference.child("Chats").child(SenderRoom!!)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postsnapshot in snapshot.children) {
                        val message = postsnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                    if (!messageList.isEmpty()) {
                        chatRecylerView.smoothScrollToPosition(messageList.size - 1)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //adding the message to database
        chatButton.setOnClickListener {

            val message = chatEditText.text.toString()

            val messageSend = Message(message, senderuid)

            databaseReference.child("Chats").child(SenderRoom!!)
                .child("messages").push().setValue(messageSend).addOnSuccessListener {

                    databaseReference.child("Chats").child(ReceiverRoom!!)
                        .child("messages").push().setValue(messageSend)
                }

            chatEditText.setText("")


        }


    }
}