package com.ece4600.mainapp;

import static java.util.UUID.fromString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;




import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Bluetooth extends Activity{
	
	private ListView listpaired, listnew;
	private ArrayAdapter<String> adapter, adapter_search;
	

	private static final long SCAN_PERIOD = 10000;  // Used to scan for devices for only 10 secs
	//Creates BLEWrapper instance
	public BleWrapper mBleWrapper = null; 
	public BleWrapper mBleWrapper2 = null; 
	private final String LOGTAG = "BLETEST";
	private String gattList = "";
	//Sensorstates
	private enum mSensorState {IDLE, ACC_ENABLE, ACC_READ, IRT_ENABLE};
    private mSensorState mState;
    private mSensorState mState2;
    public dataArray[] array_2d = new dataArray[2];
    
    //For toast messages:
    Context context;
    private Handler handler = new Handler();
    
    //TI SensorTag device info
    private final String device1_MAC = "90:59:AF:0B:82:F4";
    private final String device2_MAC = "90:59:AF:0B:82:D9";
    
    //--------------------------------------------------------------------
    // TI SensorTag UUIDs
    //--------------------------------------------------------------------
	public final static UUID 
	    UUID_IRT_SERV = fromString("f000aa00-0451-4000-b000-000000000000"),
	    UUID_IRT_DATA = fromString("f000aa01-0451-4000-b000-000000000000"),
	    UUID_IRT_CONF = fromString("f000aa02-0451-4000-b000-000000000000"), // 0: disable, 1: enable

	    UUID_ACC_SERV = fromString("f000aa10-0451-4000-b000-000000000000"),
	    UUID_ACC_DATA = fromString("f000aa11-0451-4000-b000-000000000000"),
	    UUID_ACC_CONF = fromString("f000aa12-0451-4000-b000-000000000000"), // 0: disable, 1: enable
	    UUID_ACC_PERI = fromString("f000aa13-0451-4000-b000-000000000000"), // Period in tens of milliseconds

	    UUID_HUM_SERV = fromString("f000aa20-0451-4000-b000-000000000000"),
	    UUID_HUM_DATA = fromString("f000aa21-0451-4000-b000-000000000000"),
	    UUID_HUM_CONF = fromString("f000aa22-0451-4000-b000-000000000000"), // 0: disable, 1: enable

	    UUID_MAG_SERV = fromString("f000aa30-0451-4000-b000-000000000000"),
	    UUID_MAG_DATA = fromString("f000aa31-0451-4000-b000-000000000000"),
	    UUID_MAG_CONF = fromString("f000aa32-0451-4000-b000-000000000000"), // 0: disable, 1: enable
	    UUID_MAG_PERI = fromString("f000aa33-0451-4000-b000-000000000000"), // Period in tens of milliseconds

	    UUID_BAR_SERV = fromString("f000aa40-0451-4000-b000-000000000000"), 
	    UUID_BAR_DATA = fromString("f000aa41-0451-4000-b000-000000000000"),
	    UUID_BAR_CONF = fromString("f000aa42-0451-4000-b000-000000000000"), // 0: disable, 1: enable
	    UUID_BAR_CALI = fromString("f000aa43-0451-4000-b000-000000000000"), // Calibration characteristic

	    UUID_GYR_SERV = fromString("f000aa50-0451-4000-b000-000000000000"), 
	    UUID_GYR_DATA = fromString("f000aa51-0451-4000-b000-000000000000"),
	    UUID_GYR_CONF = fromString("f000aa52-0451-4000-b000-000000000000"), // 0: disable, bit 0: enable x, bit 1: enable y, bit 2: enable z

	    UUID_KEY_SERV = fromString("0000ffe0-0000-1000-8000-00805f9b34fb"), 
	    UUID_KEY_DATA = fromString("0000ffe1-0000-1000-8000-00805f9b34fb"),
	    UUID_CCC_DESC = fromString("00002902-0000-1000-8000-00805f9b34fb");
    
    //--------------------------------------------------------------------
    // ON CREATE function
    //--------------------------------------------------------------------
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		context = this;
		
		
		createBleWrappers();
		
	    final Button bluesearch = (Button) findViewById(R.id.bluesearch);
		bluesearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				mBleWrapper.startScanning();
				
				handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    	mBleWrapper.stopScanning();
                    	Log.d(LOGTAG, "Stop Scanning");
                    }
                }, SCAN_PERIOD);
			}     
	    });
		
		//*******************	
        // Checks for BLE
        //******************* 
        if (mBleWrapper.checkBleHardwareAvailable() == false)
        {
        	Toast.makeText(this, "No BLE-compatible hardware detected",Toast.LENGTH_SHORT).show();
        	finish();
        }
		
		
	}
	
	 protected void onDestroy() {
	        super.onDestroy();
	        
	        
	        mBleWrapper.diconnect();
	        mBleWrapper.close();
	        
	        mBleWrapper2.diconnect();
	        mBleWrapper2.close();

	    }



	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listpaired.setAdapter(adapter);
		switch (v.getId()) {
		case R.id.blueon:
			break;
		case R.id.blueoff:
			//myBluetoothAdapter.disable();
			Toast.makeText(getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bluecancel:
			startActivity(new Intent(Bluetooth.this, MainActivity.class));
			finish();
		default:
			break;
		}		     
	}

    
    public void createBleWrappers(){
    	 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //*********************************************************	
        // MBLEWRAPPER FOR DEVICE 1
        //********************************************************* 
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null()
        {
        //*******************	
        // If a device is found
        //*******************
        @Override
        public void uiDeviceFound(final BluetoothDevice device,final int rssi,final byte[] record)
        {
        	String msg = "uiDeviceFound: "+device.getName()+", "+rssi+", "+ String.valueOf(rssi);
        			Log.d("DEBUG", "uiDeviceFound: " + msg);
        			
 
        		      handler.post(new Runnable(){
        					@Override
        					public void run() {
        						
        						if(device1_MAC.equals(device.getAddress())){
        							boolean status = true;
        							status = mBleWrapper.connect(device.getAddress());
        							
        							if (status == false)
        							{
        							Log.d("DEBUG", "uiDeviceFound: Connection problem");
        							}
        							else{
        								Log.d("DEBUG", "Connection successful");
        							}
        						}
        						
        					}
        		        	
        		         });

        		      
        		    
      			

        }
        //*******************	
        // Stores BLE device's services and enables them
        //*******************
        @Override
        public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List <BluetoothGattService> services)
        {
            BluetoothGattCharacteristic c;
            BluetoothGattDescriptor d;
            //Retrieves Services:
        	for (BluetoothGattService service : services)
        		{
        			String serviceName = BleNamesResolver.resolveUuid(service.getUuid().toString());
        			Log.d(LOGTAG, serviceName);
                    gattList += serviceName + "\n";

                    mBleWrapper.getCharacteristicsForService(service);
        		}
            //Enable services:
            Log.d(LOGTAG, "DEVICE 1 uiAvailableServices: Enabling services");
            c = gatt.getService(UUID_ACC_SERV).getCharacteristic(UUID_ACC_CONF);
            mBleWrapper.writeDataToCharacteristic(c, new byte[] {0x01});
            mState = mSensorState.ACC_ENABLE;
        }
        
        @Override
        public void uiCharacteristicForService(	BluetoothGatt gatt, 
                BluetoothDevice device, 
                BluetoothGattService service,
                List<BluetoothGattCharacteristic> chars) 
        {
            super.uiCharacteristicForService(gatt, device, service, chars);
            for (BluetoothGattCharacteristic c : chars)
            {
                String charName = BleNamesResolver.resolveCharacteristicName(c.getUuid().toString());
                Log.d(LOGTAG, charName);
                gattList += "Characteristic: " + charName + "\n";
            }
        }
        //*******************	
        // Successful Write function
        //******************* 
        @Override
        public void uiSuccessfulWrite(	BluetoothGatt gatt,
                                        BluetoothDevice device, 
                                        BluetoothGattService service,
                                        BluetoothGattCharacteristic ch, 
                                        String description) 
        {
            BluetoothGattCharacteristic c;

            super.uiSuccessfulWrite(gatt, device, service, ch, description);
            Log.d(LOGTAG, "DEVICE 1  uiSuccessfulWrite");

            switch (mState)
            {
            case ACC_ENABLE:
            	//Log.d(LOGTAG, "DEVICE 1 uiSuccessfulWrite: Successfully enabled accelerometer");
            	  Bluetooth.this.runOnUiThread(new Runnable() {
    		          public void run() {
    		              Toast.makeText(Bluetooth.this, "Successfully enabled DEVICE 1 accelerometers", Toast.LENGTH_SHORT).show();

    		          }
    		      });
                break;
            case ACC_READ:
                //Log.d(LOGTAG, "DEVICE 1 uiSuccessfulWrite: state = ACC_READ");					
                break;

            default:
                break;
            }
        }
        
        @Override
        public void uiFailedWrite(	BluetoothGatt gatt,
                                    BluetoothDevice device, 
                                    BluetoothGattService service,
                                    BluetoothGattCharacteristic ch, 
                                    String description) 
        {
            super.uiFailedWrite(gatt, device, service, ch, description);
            //Log.d(LOGTAG, "DEVICE 1 uiFailedWrite");
        }
        
        @Override
        public void uiNewValueForCharacteristic(BluetoothGatt gatt,
                                                BluetoothDevice device, 
                                                BluetoothGattService service,
                                                BluetoothGattCharacteristic ch, 
                                                String strValue,
                                                int intValue, 
                                                byte[] rawValue, 
                                                String timestamp,
                                                final float[] vector) 
        {
            
        	super.uiNewValueForCharacteristic(gatt, device, service, ch, strValue, intValue, rawValue, timestamp,vector);
            
            //Log.d(LOGTAG, "DEVICE 1 uiNewValueForCharacteristic");
            // decode current read operation
            switch (mState)
            {
            	case ACC_READ:
            		//Log.d(LOGTAG, "DEVICE 1 uiNewValueForCharacteristic: Accelerometer data:" + vector[0] +  "," + vector[1] +  "," + vector[2] );
            		//Sends data to main UI thread
            		
            	      handler.post(new Runnable(){
            				@Override
            				public void run() {
                				dataArray data = new dataArray(vector[0], vector[1], vector[2]);
                  				array_2d[0] = data;
            				}
            	        	
            	         });
            		
            	break;
            }
        	
          
        }
        
        @Override
        public void uiDeviceConnected(BluetoothGatt gatt, final BluetoothDevice device) 
        {
            Log.d(LOGTAG, "DEVICE 1 uiDeviceConnected: State = " + mBleWrapper.getAdapter().getState());
            Bluetooth.this.runOnUiThread(new Runnable() {
		          public void run() {
		              Toast.makeText( Bluetooth.this, "Device 1 connected" + device.getName(), Toast.LENGTH_SHORT).show();

		          }
		      });
        }

        @Override
        public void uiDeviceDisconnected(BluetoothGatt gatt, BluetoothDevice device) {
            Log.d(LOGTAG, "DEVICE 1 uiDeviceDisconnected: State = " + mBleWrapper.getAdapter().getState());	
            gatt.disconnect();
        }
        
        
        @Override
        public void uiGotNotification(	BluetoothGatt gatt,
                BluetoothDevice device, 
                BluetoothGattService service,
                BluetoothGattCharacteristic characteristic) 
        {
            super.uiGotNotification(gatt, device, service, characteristic);
            String ch = BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString());

            Log.d(LOGTAG,  "DEVICE 1 uiGotNotification: " + ch);
        }
        });
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //*********************************************************	
        // MBLEWRAPPER FOR DEVICE 2
        //********************************************************* 
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        
        mBleWrapper2 = new BleWrapper(this, new BleWrapperUiCallbacks.Null()
        {
        //*******************	
        // If a device is found
        //*******************
        	  @Override
              public void uiDeviceFound(final BluetoothDevice device,final int rssi,final byte[] record)
              {
              	String msg = "uiDeviceFound: "+device.getName()+", "+rssi+", "+ String.valueOf(rssi);
              			Log.d("DEBUG", "uiDeviceFound: " + msg);
              			
       
              		      handler.post(new Runnable(){
              					@Override
              					public void run() {
              						if(device2_MAC.equals(device.getAddress())){
            							boolean status = true;
            							status = mBleWrapper2.connect(device.getAddress());
            							
            							if (status == false)
            							{
            							Log.d("DEBUG", "uiDeviceFound: Connection problem");
            							}
            							else{
            								Log.d("DEBUG", "Connection successful");
            							}
            						}
            						
              						
              					}
              		        	
              		         });

              		      
              		    
            			

              }
              //*******************	
              // Stores BLE device's services and enables them
              //*******************
              @Override
              public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List <BluetoothGattService> services)
              {
                  BluetoothGattCharacteristic c;
                  BluetoothGattDescriptor d;
                  //Retrieves Services:
              	for (BluetoothGattService service : services)
              		{
              			String serviceName = BleNamesResolver.resolveUuid(service.getUuid().toString());
              			Log.d(LOGTAG, serviceName);
                        gattList += serviceName + "\n";
                        mBleWrapper2.getCharacteristicsForService(service);
              		}
                  //Enable services:
                  c = gatt.getService(UUID_ACC_SERV).getCharacteristic(UUID_ACC_CONF);
                  mBleWrapper2.writeDataToCharacteristic(c, new byte[] {0x01});
                  mState2 = mSensorState.ACC_ENABLE;
              }
              
              @Override
              public void uiCharacteristicForService(	BluetoothGatt gatt, 
                      BluetoothDevice device, 
                      BluetoothGattService service,
                      List<BluetoothGattCharacteristic> chars) 
              {
                  super.uiCharacteristicForService(gatt, device, service, chars);
                  for (BluetoothGattCharacteristic c : chars)
                  {
                      String charName = BleNamesResolver.resolveCharacteristicName(c.getUuid().toString());
                      Log.d(LOGTAG, charName);
                      gattList += "Characteristic: " + charName + "\n";
                  }
              }
              //*******************	
              // Successful Write function
              //******************* 
              @Override
              public void uiSuccessfulWrite(	BluetoothGatt gatt,
                                              BluetoothDevice device, 
                                              BluetoothGattService service,
                                              BluetoothGattCharacteristic ch, 
                                              String description) 
              {
                  BluetoothGattCharacteristic c;
                  super.uiSuccessfulWrite(gatt, device, service, ch, description);
                  switch (mState2)
                  {
                  case ACC_ENABLE:
                  	  Bluetooth.this.runOnUiThread(new Runnable() {
          		          public void run() {
          		              Toast.makeText(Bluetooth.this, "Successfully enabled DEVICE 2 accelerometers", Toast.LENGTH_SHORT).show();

          		          }
          		      });
                      break;
                  case ACC_READ:				
                      break;

                  default:
                      break;
                  }
              }
              
              @Override
              public void uiFailedWrite(	BluetoothGatt gatt,
                                          BluetoothDevice device, 
                                          BluetoothGattService service,
                                          BluetoothGattCharacteristic ch, 
                                          String description) 
              {
                  super.uiFailedWrite(gatt, device, service, ch, description);
              }
              //*******************	
              //WHERE YOU CAN RETRIEVE NEW ACCLEROMETER VALUES
              //******************* 
              @Override
              public void uiNewValueForCharacteristic(BluetoothGatt gatt,
                                                      BluetoothDevice device, 
                                                      BluetoothGattService service,
                                                      BluetoothGattCharacteristic ch, 
                                                      String strValue,
                                                      int intValue, 
                                                      byte[] rawValue, 
                                                      String timestamp,
                                                      final float[] vector) 
              {
                  
              	super.uiNewValueForCharacteristic(gatt, device, service, ch, strValue, intValue, rawValue, timestamp,vector);
                  
                  switch (mState2)
                  {
                  	case ACC_READ:
                  		Log.d(LOGTAG, "DEVICE 2 uiNewValueForCharacteristic: Accelerometer data:" + vector[0] +  "," + vector[1] +  "," + vector[2] );
                  		//Sends data to main UI thread
                  		
                  	      handler.post(new Runnable(){
                  				@Override
                  				public void run() {
                      				dataArray data = new dataArray(vector[0], vector[1], vector[2]);
                      				array_2d[1] = data;
                  				}
                  	         });
                  	break;
                  }
              }
              
              @Override
              public void uiDeviceConnected(BluetoothGatt gatt, final BluetoothDevice device) 
              {
                  Bluetooth.this.runOnUiThread(new Runnable() {
      		          public void run() {
      		              Toast.makeText(Bluetooth.this, "Device 2 connected" + device.getName(), Toast.LENGTH_SHORT).show();

      		          }
      		      });
              }

              @Override
              public void uiDeviceDisconnected(BluetoothGatt gatt, BluetoothDevice device) {
                  gatt.disconnect();
              }
              
              
              @Override
              public void uiGotNotification(	BluetoothGatt gatt,
                      BluetoothDevice device, 
                      BluetoothGattService service,
                      BluetoothGattCharacteristic characteristic) 
              {
                  super.uiGotNotification(gatt, device, service, characteristic);
                  String ch = BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString());
              }
              });
    }
}