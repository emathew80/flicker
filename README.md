# Flickr Clone for Malauzai

## Introduction

> Create an Android application on Android Studio (2.2, appcompat-v7:25.+) that presents the user a set of images downloaded from Flick public JSON stream. The user will be able to swipe between images and use a swipe to refresh pull from the top of the screen to reload a new set of images from Flickr.

## Code Samples

>   Async method to grab JSON from Flickr API.

class fetchData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) { //Run API request in background
            StringBuffer buffer = new StringBuffer();

            try {
                URL url = new URL("https://api.flickr.com/services/feeds/photos_public.gne?format=json"); // Set URL
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //Open the connection
                InputStream inputStream = httpURLConnection.getInputStream(); //Grab input stream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //Initialize buffered reader

                String line = "";

                //Read line when available and add to buffer string
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                return buffer.toString(); //return the final string

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            Url = str;
            getMediaUrlAndDisplayImage(str); //set the image on main activity
        }
    }

## Installation

> Clone repo, open in Android Studio, press play. Emulated on Nexus 6. Tested on emulator only (development S6 was out of commission).
