#A tiny library for Rate Us Dialog

![Sample](https://github.com/e-koc/rate-me-dialog/blob/develop/app/src/main/res/drawable/rate_sample.png)


#Usage

**Register in your Application class** 

**`RateMe.register(this, 24);`**

24 means that dialog will be shown every 24 hours until user clicks **positive** or **neutral** buttons


Then call wherever you want like
**`RateMe.checkForShow(this);`**

checkForShow shows dialog only when its time. Otherwise it doesn't do anything

If you want to show dialog immediately, use **`RateMe.showImmediately(this);`**



### Also you can override dialog texts in your project


`<string name="rate_me_button_positive">Rate</string>`

`<string name="rate_me_button_negative">Later</string>`

`<string name="rate_me_button_never_show_again">Never Show</string>`

`<string name="rate_me_dialog_title"></string>`

`<string name="rate_me_dialog_message">Would you like to rate us on Google Play?</string>`
