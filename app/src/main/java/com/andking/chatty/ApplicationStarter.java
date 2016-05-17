/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.andking.chatty;

import android.app.Application;

import com.parse.Parse;


public class ApplicationStarter extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(this);

   // ParseUser.enableAutomaticUser();
   // ParseACL defaultACL = new ParseACL();


    // Optionally enable public read access.
    // defaultACL.setPublicReadAccess(true);
   // ParseACL.setDefaultACL(defaultACL, true);
  }
}
