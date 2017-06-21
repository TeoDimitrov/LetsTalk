package com.wecode.letstalk.configuration;

public final class Config {

    public final static String CHILD_CHATS = "chats";

    public final static String CHILD_USERS = "users";

    public final static String CHILD_USERS_EMAIL = "email";

    public final static String CHILD_TIMEFRAMES = "timeframes";

    public final static String CHILD_USERS_ROLE = "role";

    public final static String CHILD_USERS_ROLE_NAME = "name";

    public final static String USER_SUFIX = "@letstalk";

    public final static String USER_AUTHOR_EXTRA = "author";

    public static final String USER_RECIPIENT_EXTRA = "recipient";

    public static final String USER_SCHEDULE = "schedule";

    public static final String CHAT_PATH_EXTRA = "chat_path";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final int REQUEST_RETURN_CLIENT = 2;

    public static final int REQUEST_RETURN_SCHEDULE = 3;

    public static final int REQUEST_CODE_SPEECH_INPUT = 4;

    public static final int REQUEST_MICROPHONE = 5;

    public static final int REQUEST_RETURN_ACCOUNT = 6;

    public static final String SINCH_HOSTNAME = "sandbox.sinch.com";

    public static final String SINCH_KEY = "8fbfec34-622d-4eb1-898a-a25a0c2338f1";

    public static final String SINCH_SECRET = "NcryiitcLEKBZxUiwoARPw==";

    public static final String SINCH_CALLER_ID = "CALLER_ID";

    public static final String SINCH_RECIPIENT_ID = "RECIPIENT_ID";

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final String VALID_EMAIL_ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public static final String VALID_TOPIC_REGEX = "^([a-zA-Z0-9-_.~%]{1,900})$";

    public static final String ERROR_EXISTING_USER = "Email already exists.";

    public static final String ERROR_NO_SUCH_USER = "Wrong credentials.";

    public static final String ERROR_EMAIL_IS_REQUIRED = "Email is required.";

    public static final String ERROR_EMAIL_NOT_VALID = "Email not valid.";

    public static final String ERROR_PASSWORD_IS_REQUIRED = "Password is required.";

    public static final String ERROR_PASSWORD_SHORT = "Enter at least 6 symbols.";

    public static final String ERROR_CONFIRM_PASSWORD_IS_REQUIRED = "Confirm password is required.";

    public static final String ERROR_PASSWORDS_DOESNT_MATCH = "Passwords does not match.";

    public static final String ERROR_BIRTHYEAR_NOT_NUMBER = "Birth mYear should be a number.";

    public static final String MESSAGE_AUTHENTICATION = "Authentication";

    public static final String MESSAGE_CREATING_USER = "Creating User...";

    public static final String ERROR_BIRTHYEAR_IS_REQUIRED = "Birth year is required.";

    public static final String ERROR_YOUNGER_THAN_15 = "You should be older than 15";

    public static final String ERROR_OLDER_THAN_100 = "You should be younger than 100";

    public static final String ERROR_EMAIL_SHOULD_NOT_CONTAIN = "Email should not contain #, $, [ or ]";

    public static final String ERROR_TEXT_TOO_LONG = "Message is too long.";

    public static final int MESSAGE_MAX_LENGHT = 500;

    public static final String ADVISOR_TITLE = "Advisor";

    public static final String ADVISOR_ROLE = "AdvisorRole";

    public static final String ADVISOR_NAME = "advisorName";

    public static final String NOTIFICATION_BROADCAST = "com.example.letstalk.NOTIFICATION_BROADCAST";

    public static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    public static final String FCM_REMOTE_MESSAGE_TO = "to";

    public static final String FCM_REMOTE_MESSAGE_DATA = "data";

    public static final String FCM_TOPIC_SUBSCRIBE = "/topics/";

    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static final String FCM_CONTENT_TYPE = "application/json";

    public static final String FCM_AUTHORIZATION = "key=AAAAMKBdr_8:APA91bHltbHYyMBrPGwK7yoFgTgyvDK91RctRHSyxntTjozGzIgv4oNw5hT3yniUsJzhtiKgTrhZS6tRdR0AXqBxzc5YEK9IL-8NYtwl66jEHXQBDj94tH6x4vYg9LWMmxXicEtegvX0";

    public static final String NO_AVAILABLE_ADVISOR = "No available advisors. Try different time slot";

    public static final String ADVISOR_ACTIVATION = "Reopen the app to become an advisor";

    public static final String USER_BIRTHYEAR = "birthyear";

    public static final String USER_GENDER = "gender";
}
