package com.m2comm.asthma;

public interface FileUploadInterface {
	public void onPreExecute();
	public void doInBackground();
	public void onPostExecute(String str);
	public void exeception(String str);
}
