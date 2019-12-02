package com.bvrk.mobile.android.util;

public class Configdat {
    public static final String APP_CONTACT = "+919676867570";

    public static final String TBL_MODELS    = "models";
    public static final String TBL_COLORS    = "color";
    public static final String TBL_MODELDATA = "pattern";

    public static final String TBL_MODELSLIST = "models";
    public static final String TBL_MODELIMG   = "pattern";

    public static final String FILE_MODELS    = "1";
    public static final String FILE_COLOR     = "2";
    public static final String FILE_MODELDATA = "3";

    public static final String FILE_MODELSLIST = "1";
    public static final String FILE_MODELIMG   = "3";

    public static final String fp_key="MLz5RUXq9$gh"; // 9 char = 128 bit
    public static final String my_key="MLz5RUXqBJpTQmJs"; // 16 char = 128 bit
    //public static final String my_key="MLz5RUXq9"; // 9 char = 128 bit
    public static final String my_spec_key="sdgs9dgsd";
    public static final String tmpfile="tmpmax";

    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final String IMAGE_FILE="png gif jpg jpeg bmp tiff tif icon ";
    public static final String AUDIO_FILE = "mp3 mp2 mpga m4a m4p wav ogg mid midi amr aac m3u ram ra aif aiff aifc ";
    public static final String VIDEO_FILE = "mpeg mpg mpe mov qt mp4 3gp 3gpp m4u mxu flv wmx avi ";

    public static final String[] PACKAGE_FILE = {"jar", "zip", "rar", "gz"};
    public static final String[] PACKAGE_FILE_EXT = {"application/java-archive", "application/zip", "application/x-rar-compressed", "application/gzip"};

    public static final String[] BROWSER_FILE = {"htm", "html", "php", "css", "js"};
    public static final String[] BROWSER_FILE_EXT = {"text/htm", "text/html", "text/php", "text/html", "text/html"};

    public static final String[] TEXT_FILE = {"txt", "rtf", "csv", "xml"};
    public static final String[] TEXT_FILE_EXT = {"text/plain", "text/rtf", "text/csv", "text/xml"};

    public static final String[] DOC_FILE = {"doc", "docx", "ppt", "xls", "pdf"};
    public static final String[] DOC_FILE_EXT = {"application/msword", "application/msword", "application/vnd.ms-powerpoint", "application/vnd.ms-excel", "application/pdf"};

    public static final String[] APK_FILE = {"apk"};
    public static final String[] APK_FILE_EXT = {"application/vnd.android.package-archive"};

    public static final String MAPP = "/tkit";
    public static final String DATA = MAPP+"/data";

    public static final String SAVE_FOLDER_NAME =MAPP+"/Carin-customer-pics";

    public static final String CARIN_MODELS =MAPP+"/models";


    public static final String APPDIR = "/WhatsApp/Media";

    public static final String WA_STATUS = APPDIR+"/.Statuses";

    public static final String WA_WDOCS = APPDIR+"/WhatsApp Documents";
    public static final String WA_WDOCS_SENT = APPDIR+"/WhatsApp Documents/Sent";

    public static final String WA_WIMAGES = APPDIR+"/WhatsApp Images";
    public static final String WA_WIMAGES_SENT = APPDIR+"/WhatsApp Images/Sent";

    public static final String WA_VIDEOS = APPDIR+"/WhatsApp Video";
    public static final String WA_VIDEOS_SENT = APPDIR+"/WhatsApp Video/Sent";

    public static final String WA_AUDIO = APPDIR+"/WhatsApp Audio";
    public static final String WA_AUDIO_SENT = APPDIR+"/WhatsApp Audio/Sent";

    public static final String WA_ANIMATE = APPDIR+"/WhatsApp Animated Gifs";
    public static final String WA_ANIMATE_SENT = APPDIR+"/WhatsApp Animated Gifs/Sent";

    /*
    int index = -1;
    for (int i=0;i<TYPES.length;i++) {
    if (TYPES[i].equals(carName)) {
        index = i;
        break;
    }
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
intent.setType("text/html");
// intent.setType("text/plain");
final PackageManager pm = getPackageManager();
final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
ResolveInfo best = null;
for (final ResolveInfo info : matches) {
    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
        best = info;
        break;
    }
}
if (best != null) {
    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
}
intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "YOUR SUBJECT");
intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("YOUR EXTRAS"));

startActivity(intent);

try {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        context.startActivity(intent);
} catch (android.content.ActivityNotFoundException anfe) {
        handleException();
}
     */

}
