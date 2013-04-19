package com.example.willowtreeproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.TimeSpanConverter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
        

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
	}


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			switch (position) {
			case 0:
				args.putString(DummySectionFragment.ARG_USER_NAME, "heybluez");
				break;
			case 1:
				args.putString(DummySectionFragment.ARG_USER_NAME, "rockmaninoff");
				break;
			case 2:
				args.putString(DummySectionFragment.ARG_USER_NAME, "bsirach");
				break;
			case 3:
				args.putString(DummySectionFragment.ARG_USER_NAME, "willowtreeapps");
				break;
			case 4:
				args.putString(DummySectionFragment.ARG_USER_NAME, "jonba");
				break;
			case 5:
				args.putString(DummySectionFragment.ARG_USER_NAME, "richie681");
				break;
			}
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 6 total pages.
			return 6;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			case 5:
				return getString(R.string.title_section6).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String ARG_USER_NAME = "user_name";
		public String userName = null;
		public ProgressDialog m_ProgressDialog = null; 
	    public ArrayList<Order> m_orders = null;
	    public ArrayList<twitUser> m_Users = null;
	    public OrderAdapter m_adapter;
	    public ViewGroup ourContainer;
		public LayoutInflater ourInflater;

        public ImageView userPic;
        public TextView following;
        public TextView followers;
        public TextView tweets;
		
		public DummySectionFragment(){
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        this.ourContainer = container;
	        this.ourInflater = inflater;
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			
			//TextView dummyTextView = (TextView) rootView.findViewById(R.id.following);

	        this.userPic = (ImageView) rootView.findViewById(R.id.user_Icon);
	        this.following = (TextView) rootView.findViewById(R.id.following);
	        this.followers = (TextView) rootView.findViewById(R.id.followers);
	        this.tweets = (TextView) rootView.findViewById(R.id.tweets);
			Bundle args = getArguments();
			System.out.println("Current Number: " + args.getInt(ARG_SECTION_NUMBER));
			System.out.println("Current Name: " + args.getString(ARG_USER_NAME));
			this.userName = args.getString(ARG_USER_NAME);
			m_orders = new ArrayList<Order>();
	        m_Users = new ArrayList<twitUser>();
	        this.m_adapter = new OrderAdapter(getActivity(), inflater, R.layout.row, m_orders);	        
	        //setListAdapter(this.m_adapter);
	        
	        ListView mListView;
	        mListView = (ListView) rootView.findViewById(R.id.list);
	        mListView.setAdapter(this.m_adapter);
	        
	        new DummySectionFragment.AsyncTest().execute();
	      
	        //m_ProgressDialog = ProgressDialog.show(getActivity(),    
	           //   "Please wait...", "Retrieving data ...", true);
	        
	        
			return rootView;
		}
		
		public class AsyncTest extends AsyncTask<String, Void, String> {

	        @Override
	        protected String doInBackground(String... params) {
	        	try {
	                // gets Twitter instance with default credentials
	            	ConfigurationBuilder cb = new ConfigurationBuilder();
	            	cb.setDebugEnabled(true)
	            	  .setOAuthConsumerKey("5g0ohOlHQulDQSiqGGEaiQ")
	            	  .setOAuthConsumerSecret("GSJW7lybDBCr0IaHzAFLWrbaQjIEacN4vho58aPns")
	            	  .setOAuthAccessToken("391075607-T1HUenmic29Bx9gCT8pfsbo41ve104fNfQBEQErM")
	            	  .setOAuthAccessTokenSecret("xPVu43tujVnqMJN53r2BsFdAYowTDmyMAujt76eln0k");
	            	TwitterFactory tf = new TwitterFactory(cb.build());
	            	Twitter twitter = tf.getInstance();
	                Bitmap bitmap = null;
	                String user = userName;
	                List<twitter4j.Status> statuses = twitter.getUserTimeline(user);
	                User twitUser = twitter.showUser(user);
	        		try {
	            		  bitmap = BitmapFactory.decodeStream((InputStream)new URL(twitUser.getBiggerProfileImageURL()).getContent());
	            		} catch (MalformedURLException e) {
	            		  e.printStackTrace();
	            		} catch (IOException e) {
	            		  e.printStackTrace();
	            		}
	        		twitUser curUser = new twitUser(twitUser.getScreenName(), twitUser.getFriendsCount(), twitUser.getFollowersCount(), twitUser.getStatusesCount(), bitmap);
	                m_Users.add(curUser);
	        		Order o1;
	                for (twitter4j.Status status : statuses) {
	                   	if(status.isRetweet()){
	                   		status = status.getRetweetedStatus();
	                		//System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " -- " + status.getUser().getProfileImageURL());
	                		try {
	                  		  bitmap = BitmapFactory.decodeStream((InputStream)new URL(status.getUser().getBiggerProfileImageURL()).getContent());
	                  		} catch (MalformedURLException e) {
	                  		  e.printStackTrace();
	                  		} catch (IOException e) {
	                  		  e.printStackTrace();
	                  		}
	                    	o1 = new Order(status.getCurrentUserRetweetId(), status.getUser().getName(), status.getText(), status.getCreatedAt(), bitmap);
	                	}else{
	                		//System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()); 	
	                		try {
	                			bitmap = BitmapFactory.decodeStream((InputStream)new URL(status.getUser().getBiggerProfileImageURL()).getContent());
	                  		} catch (MalformedURLException e) {
	                  		  e.printStackTrace();
	                  		} catch (IOException e) {
	                  		  e.printStackTrace();
	                  		}
	                		o1 = new Order(status.getId(), status.getUser().getName(), status.getText(), status.getCreatedAt(), bitmap);
	                	}
	                    m_orders.add(o1);
	                }
	            } catch (TwitterException te) {
	                te.printStackTrace();
	                System.out.println("Failed to get timeline: " + te.getMessage());
	                System.exit(-1);
	            }
	        	return null;
	        }      

	        @Override
	        protected void onPostExecute(String result) {
	            if(m_orders != null && m_orders.size() > 0){
	            	m_adapter.setItems(m_orders);
	                m_adapter.notifyDataSetChanged();
	            }
	            //m_ProgressDialog.dismiss();
	            m_adapter.notifyDataSetChanged();
	        	setUserData(m_Users, ourInflater, ourContainer);
	        }

	        @Override
	        protected void onPreExecute() {
	        }

	        @Override
	        protected void onProgressUpdate(Void... values) {
	        }
	    }
		
		public void setUserData(ArrayList<twitUser> users, LayoutInflater inflater, ViewGroup container){
			twitUser inUser = users.get(0);
	        System.out.println("User checking: " + inUser.getUserName());
	        if(userPic != null){
	            userPic.setImageBitmap(inUser.getUserPic());
	        } 
	        if(following != null){
	        	String first = inUser.getUserFollowing();
	        	String next = " Following";
	        	following.setText(first + next, BufferType.SPANNABLE);
		        Spannable s = (Spannable)following.getText();
		        int start = 0;
		        int end = first.length();
		        s.setSpan(new ForegroundColorSpan(Color.rgb(136, 194, 200)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        }
	        if(followers != null){
	        	String first = inUser.getUserFollowers();
	        	String next = " Followers";
	        	followers.setText(first + next, BufferType.SPANNABLE);
		        Spannable s = (Spannable)followers.getText();
		        int start = 0;
		        int end = first.length();
		        s.setSpan(new ForegroundColorSpan(Color.rgb(136, 194, 200)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        }
	        if(tweets != null){
	        	tweets.setTextColor(Color.rgb(136, 194, 200));
	        	tweets.setText(" " + inUser.getUserTweets() + " TWEETS");
	        }
	    }
		
		class OrderAdapter extends ArrayAdapter<Order> {

	        private ArrayList<Order> items;
	        LayoutInflater inflater;

	        public OrderAdapter(Context dummySectionFragment, LayoutInflater inflater, int textViewResourceId, ArrayList<Order> items) {
	                super(dummySectionFragment, textViewResourceId, items);
	                this.items = items;
	                this.inflater = inflater;
	        }
	        

			public void setItems(ArrayList<Order> items) {
	        	this.items = items;
	        }
	        
	        public int getCount(){
	        	return this.items == null ? 0 : this.items.size();
	        }
	        
	        public long getItemId(final int position){
	        	return position;
	        }
	        
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = inflater;
	                    v = vi.inflate(R.layout.row, null);
	                }
	                Order o = items.get(position);
	                twitter4j.util.TimeSpanConverter tSpan = new TimeSpanConverter();
	                if (o != null) {
	                        TextView tt = (TextView) v.findViewById(R.id.toptext);
	                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
	                        TextView ts = (TextView) v.findViewById(R.id.timetext);
	                        ImageView up = (ImageView) v.findViewById(R.id.icon);
	                        if (tt != null) {	
	                              tt.setText(o.getOrderName());                            }
	                        if(bt != null){
	                              bt.setText(o.getOrderStatus());
	                              bt.setAutoLinkMask(0); 
	                              Linkify.addLinks(bt, Linkify.WEB_URLS);
	                              // A transform filter that simply returns just the text captured by the
	                              // first regular expression group.
	                              TransformFilter mentionFilter = new TransformFilter() {
	                                  public final String transformUrl(final Matcher match, String url) {
	                                      return match.group(1);
	                                  }
	                              };

	                              // Match @mentions and capture just the username portion of the text.
	                              Pattern pattern = Pattern.compile("@([A-Za-z0-9_-]+)");
	                              String scheme = "http://twitter.com/";
	                              Linkify.addLinks(bt, pattern, scheme, null, mentionFilter);
	                              Linkify.addLinks(bt, 0);
	                        }
	                        if(ts != null){
	                        	  ts.setText(tSpan.toTimeSpanString(o.getOrderTime()));
	                        }
	                        if(up != null){
		                        up.setImageBitmap(o.getOrderPic());
	                        }
	                }
	                return v;
	        }
	    }
		
	}
	
	
	
	

}
