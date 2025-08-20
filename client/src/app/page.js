"use client";
import React,{useState,useEffect} from 'react';
import { Client } from '@stomp/stompjs';

export default function Home() {
    const[msg,setMsg] = useState('');
    const[msgs,setMsgs] = useState([]);
    const [stompClient, setStompClient] = useState(new Client({
              brokerURL : 'ws://localhost:8080/chat-websocket'
    }));

    useEffect(() => {
        const client = new Client({
            brokerURL : 'ws://localhost:8080/chat-websocket',
            onConnect : () => {
                console.log('Successfully connected to Websocket');
                client.subscribe('/topic/chats',   (message) => {
                        console.log('New message received:', message.body);
                        setMsgs((prevMsgs) => [...prevMsgs,message.body]);
                        msgs.forEach((msg) => {
                                console.log(msg);
                                console.log("message received");
                        });
                });
            },
            onStompError: frame => {
                        console.error('Broker reported error: ' + frame.headers['message']);
                        console.error('Additional details: ' + frame.body);
            },
        });
        console.log('Activating STOMP connection...');
        client.activate();
        setStompClient(client);
        return () => {
            if(client){
                client.deactivate();
            }
        }
    },[]);


    function sendMsg()
    {
    console.log('before sending');
    msgs.forEach((msg) => {
                                    console.log(msg);
                                    console.log("message received");
                            });
        if(stompClient && msg){
                console.log("sending message");
                stompClient.publish({
                destination : "/app/chat",
                body: JSON.stringify({"message": msg})
                });

        }

    }

  return (
    <div className='h-screen flex flex-col'>
          <div className='msgs-container h-4/5 overflow-scroll'>
              {msgs.map((chat, index) => (
                  <div key={index} className="m-5 text-right">
                      {chat}
                  </div>
              ))}
          </div>
          <div className='h-1/5 flex items-center justify-center'>
          <div className="relative">
                                <input id = "textbox" type="text"
                                        value={msg}
                                        onChange={(e) => setMsg(e.target.value)}
                                        placeholder="Type your text here"
                                        required
                                        className="block w-full p-4 ps-10 text-sm text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"  />
                                <button onClick={sendMsg} type="button"
                                        className="text-white absolute end-2.5 bottom-2.5 bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-4 py-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                                        Send
                                </button>
                            </div>

          </div>
      </div>
  );
}
