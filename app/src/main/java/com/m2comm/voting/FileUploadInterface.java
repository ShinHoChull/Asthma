package com.m2comm.voting;

public interface FileUploadInterface {
	public void onPreExecute();
	public void doInBackground();
	public void onPostExecute(int str);
	public void exeception(int str);
}
