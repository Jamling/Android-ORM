/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.os;

/**
 * A Handler allows you to send and process {@link Message} and Runnable objects
 * associated with a thread's {@link MessageQueue}. Each Handler instance is
 * associated with a single thread and that thread's message queue. When you
 * create a new Handler, it is bound to the thread / message queue of the thread
 * that is creating it -- from that point on, it will deliver messages and
 * runnables to that message queue and execute them as they come out of the
 * message queue.
 * 
 * <p>
 * There are two main uses for a Handler: (1) to schedule messages and runnables
 * to be executed as some point in the future; and (2) to enqueue an action to
 * be performed on a different thread than your own.
 * 
 * <p>
 * Scheduling messages is accomplished with the {@link #post},
 * {@link #postAtTime(Runnable, long)}, {@link #postDelayed},
 * {@link #sendEmptyMessage}, {@link #sendMessage}, {@link #sendMessageAtTime},
 * and {@link #sendMessageDelayed} methods. The <em>post</em> versions allow you
 * to enqueue Runnable objects to be called by the message queue when they are
 * received; the <em>sendMessage</em> versions allow you to enqueue a
 * {@link Message} object containing a bundle of data that will be processed by
 * the Handler's {@link #handleMessage} method (requiring that you implement a
 * subclass of Handler).
 * 
 * <p>
 * When posting or sending to a Handler, you can either allow the item to be
 * processed as soon as the message queue is ready to do so, or specify a delay
 * before it gets processed or absolute time for it to be processed. The latter
 * two allow you to implement timeouts, ticks, and other timing-based behavior.
 * 
 * <p>
 * When a process is created for your application, its main thread is dedicated
 * to running a message queue that takes care of managing the top-level
 * application objects (activities, broadcast receivers, etc) and any windows
 * they create. You can create your own threads, and communicate back with the
 * main application thread through a Handler. This is done by calling the same
 * <em>post</em> or <em>sendMessage</em> methods as before, but from your new
 * thread. The given Runnable or Message will then be scheduled in the Handler's
 * message queue and processed when appropriate.
 */
public class Handler {
    
}
