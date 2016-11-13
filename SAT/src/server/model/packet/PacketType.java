/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.packet;


/**
 *
 * @author Ega Prianto
 */
public enum PacketType {
    CHAT_SEND,
    CHAT_SEND_RESPONSE,
    CREATE_GROUP,
    DEFAULT_RESPONSE,
    GET_CHAT_REQUEST,
    GET_CHAT_RESPONSE,
    GET_GROUP_REQUEST,
    GET_GROUP_RESPONSE,
    GET_KEY,
    GET_ONLINE_RESPONSE,
    GET_ONLINE_REQUEST,
    GOT_ONLINE,
    KEY,
    LOGIN_SERVER,
    LOGIN_RESPONSE,
    LOGIN_REQUEST,
    LOGOUT,
    REGISTER
}
