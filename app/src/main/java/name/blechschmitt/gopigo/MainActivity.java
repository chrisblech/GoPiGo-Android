package name.blechschmitt.gopigo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// import org.glassfish.tyrus.client.ClientManager;
// import org.glassfish.grizzly.Transport;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;


public class MainActivity extends ActionBarActivity {

    private static Object waitLock = new Object();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendMsg();
    }

    public void sendMsg() {
        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                String URL = "echo.websocket.org";
                // String URL = "192.168.178.37:80/robot_control/info";

                try {

                    List<Transport> transports = new ArrayList<>(2);
                    transports.add(new WebSocketTransport(StandardWebSocketClient()));
                    transports.add(new RestTemplateXhrTransport());

                    SockJsClient sockJsClient = new SockJsClient(transports);
                    sockJsClient.doHandshake(new MyWebSocketHandler(), "ws://" + URL +"/sockjs");

                    /*
                    final ClientManager client = ClientManager.createClient();

                    Log.i("TYRUS-TEST", "### 1 AsyncTask.doInBackground");
                    client.connectToServer(new Endpoint() {
                        @Override
                        public void onOpen(Session session, EndpointConfig EndpointConfig) {

                            try {
                                session.addMessageHandler(new MessageHandler.Whole<String>() {
                                    @Override
                                    public void onMessage(String message) {
                                        Log.i("TYRUS-TEST", "### 3 Tyrus Client onMessage: " + message);
                                    }
                                });

                                Log.i("TYRUS-TEST", "### 2 Tyrus Client onOpen");
                                session.getBasicRemote().sendText("Do or do not, there is no try.");
                            } catch (IOException e) {
                                // do nothing
                            }
                        }
                    }, ClientEndpointConfig.Builder.create().build(), URI.create("ws://" + URL));
                    */

                } catch (DeploymentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        asyncTask.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
