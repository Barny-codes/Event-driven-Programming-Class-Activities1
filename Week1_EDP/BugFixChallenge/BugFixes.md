Bug 1: Blocking the Event Dispatch Thread (EDT)

1. Problem & Symptoms

Symptom: Clicking the "Process" button freezes the entire application interface for 5 seconds. The UI becomes un-clickable and unresponsive to system paint requests.

Root Cause: The 5-second Thread.sleep() operation was called directly inside the ActionListener callback. Action listeners run on the Event Dispatch Thread (EDT), which is responsible for rendering the UI and dispatching user input events. Blocking this thread freezes the UI.

2. Fix Strategy

Offload the long-running operation to a separate background thread using new Thread().

Use SwingUtilities.invokeLater() to post UI modifications back to the EDT once the task completes.

3. Code Comparison

// BEFORE (Buggy)
processButton.addActionListener(e -> {
    try {
        Thread.sleep(5000); // Blocks the EDT!
    } catch (InterruptedException ex) {
        ex.printStackTrace();
    }
    statusLabel.setText("Done!");
});

// AFTER (Fixed)
processButton.addActionListener(e -> {
    statusLabel.setText("Processing...");
    processButton.setEnabled(false); // Prevent multiple concurrent runs

    new Thread(() -> {
        try {
            Thread.sleep(5000); // Long task runs in background thread
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Done!");
            processButton.setEnabled(true);
        });
    }).start();
});


Bug 2: Missing Action Listener

1. Problem & Symptoms

Symptom: Clicking the "Increment" button produces no response; the counter value remains at Count: 0.

Root Cause: The JButton instance was created and added to the layout container, but no ActionListener was attached to handle user click events.

2. Fix Strategy

Attach an ActionListener to incrementButton using a lambda expression (e -> { ... }) to increment the internal counter variable and refresh the JLabel text on every button click.

3. Code Comparison

// BEFORE (Buggy)
incrementButton = new JButton("Increment");
// Missing listener!

// AFTER (Fixed)
incrementButton = new JButton("Increment");
incrementButton.addActionListener(e -> {
    count++;
    countLabel.setText("Count: " + count);
});


Bug 3: Updating Swing Components from a Background Thread

1. Problem & Symptoms

Symptom: The time label updates, but the application might experience erratic behavior, visual tearing, missing repaint events, or random NullPointerException errors.

Root Cause: Swing component methods (such as JLabel.setText()) are not thread-safe. Modifying Swing components directly from an arbitrary background thread violates the single-thread rule of Swing and leads to race conditions with the EDT.

2. Fix Strategy

Wrap all calls modifying Swing UI components inside SwingUtilities.invokeLater() when calling them from non-EDT background threads.

3. Code Comparison

// BEFORE (Buggy)
new Thread(() -> {
    while (running) {
        // Direct call from background thread!
        timeLabel.setText("Time: " + new Date()); 
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) { ... }
    }
}).start();

// AFTER (Fixed)
new Thread(() -> {
    while (running) {
        String currentFormattedTime = new Date().toString();

        // Safely dispatch execution back to the EDT
        SwingUtilities.invokeLater(() -> {
            timeLabel.setText("Time: " + currentFormattedTime);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) { ... }
    }
}).start();


Bug 4: Memory Leak via Retained References

1. Problem & Symptoms

Symptom: Clicking "Remove All" clears the visual layout, but application memory consumption remains high or steadily grows over time.

Root Cause: Calling buttonPanel.removeAll() removes components from the visual container hierarchy, but the backing Java list (createdButtons) maintains strong object references to every created JButton. The Java Garbage Collector (GC) cannot collect objects that still have active references.

2. Fix Strategy

Call createdButtons.clear() alongside buttonPanel.removeAll() so all strong references to deleted UI elements are released.

3. Code Comparison

// BEFORE (Buggy)
removeButton.addActionListener(e -> {
    buttonPanel.removeAll();
    buttonPanel.revalidate();
    buttonPanel.repaint();
    // createdButtons list still holds all JButton references!
});

// AFTER (Fixed)
removeButton.addActionListener(e -> {
    buttonPanel.removeAll();
    buttonPanel.revalidate();
    buttonPanel.repaint();

    // Release all object references for Garbage Collection
    createdButtons.clear();
});


In Summary 
Bug 1: Blocking the EDT

Symptom: UI freezes when button clicked

Cause: Long operation runs on EDT

Fix: Run operation in background thread, update UI on EDT

Code Change: Wrapped Thread.sleep(5000) inside new Thread(() -> { ... }).start() and wrapped statusLabel.setText("Done!") inside SwingUtilities.invokeLater(). Also disabled the process button during execution.

Bug 2: Missing Listener

Symptom: Button click does nothing

Cause: No ActionListener registered

Fix: Added ActionListener to increment button

Code Change: Called incrementButton.addActionListener(...) to increment the count variable and update countLabel.setText("Count: " + count) on click events.

Bug 3: Wrong Thread

Symptom: Erratic behavior, potential crashes

Cause: GUI updated from background thread

Fix: Used SwingUtilities.invokeLater() for GUI updates

Code Change: Enclosed timeLabel.setText(...) inside SwingUtilities.invokeLater(() -> { ... }) within the background thread loop to ensure thread-safe Swing execution on the EDT.

Bug 4: Memory Leak

Symptom: Memory usage grows over time

Cause: References kept in list after removal from UI

Fix: Clear the list when removing buttons

Code Change: Added createdButtons.clear() inside the removeButton action listener alongside buttonPanel.removeAll() to dereference deleted buttons for garbage collection.