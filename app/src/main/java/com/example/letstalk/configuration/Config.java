package com.example.letstalk.configuration;

public final class Config {

    public final static String CHILD_CHATS = "chats";

    public final static String CHILD_USERS = "users";

    public final static String CHILD_USERS_EMAIL = "email";

    public final static String CHILD_TIMEFRAMES = "timeframes";

    public final static String USER_SUFIX = "@letstalk";

    public final static String USER_EXTRA = "user";

    public static final String CLIENT_USER_EXTRA = "client";

    public static final String CHAT_EXTRA = "chat";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final int REQUEST_RETURN_CLIENT = 2;

    public static final int REQUEST_USE_SIP = 3;

    public static final int REQUEST_CODE_SPEECH_INPUT = 4;

    public static final int REQUEST_MICROPHONE = 5;

    public static final String SINCH_HOSTNAME = "sandbox.sinch.com";

    public static final String SINCH_KEY = "8fbfec34-622d-4eb1-898a-a25a0c2338f1";

    public static final String SINCH_SECRET = "NcryiitcLEKBZxUiwoARPw==";

    public static final String SINCH_CALLER_ID = "CALLER_ID";

    public static final String SINCH_RECIPIENT_ID = "RECIPIENT_ID";

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final String VALID_EMAIL_ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    
    public static final String ERROR_EXISTING_USER = "Email already exists.";
    
    public static final String ERROR_NO_SUCH_USER = "Wrong credentials.";
    
    public static final String ERROR_EMAIL_IS_REQUIRED = "Email is required.";
    
    public static final String ERROR_EMAIL_NOT_VALID = "Email not valid.";
    
    public static final String ERROR_PASSWORD_IS_REQUIRED = "Password is required.";
    
    public static final String ERROR_PASSWORD_SHORT = "Enter at least 6 symbols.";
    
    public static final String ERROR_CONFIRM_PASSWORD_IS_REQUIRED = "Confirm password is required.";
    
    public static final String ERROR_PASSWORDS_DOESNT_MATCH = "Passwords does not match.";

    public static final String ERROR_BIRTHYEAR_NOT_NUMBER = "Birth year should be a number.";

    public static final String MESSAGE_AUTHENTICATION = "Authentication";
    
    public static final String MESSAGE_CREATING_USER = "Creating User...";
    
    public static final String  MESSAGE_AUTHENTICATING = "Authenticating...";

    public static final String ERROR_BIRTHYEAR_IS_REQUIRED = "Birth year is required.";

    public static final String ERROR_YOUNGER_THAN_15 = "You should be older than 15";

    public static final String ERROR_OLDER_THAN_100 = "You should be younger than 100";

    public static final String ERROR_EMAIL_SHOULD_NOT_CONTAIN = "Email should not contain #, $, [ or ]";

    public static final String ERROR_TEXT_TOO_LONG = "Message is too long.";

    public static final int MESSAGE_MAX_LENGHT = 500;

    public static final String ADVISOR_TITLE = "Advisor";

    public static final int NOTIFICATION_CHAT = 1;
}
