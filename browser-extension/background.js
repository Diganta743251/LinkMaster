// Background script for LinkVault extension

// Install event
chrome.runtime.onInstalled.addListener(() => {
    console.log('LinkVault extension installed');
    
    // Create context menu
    chrome.contextMenus.create({
        id: 'saveLinkVault',
        title: 'Save to LinkVault',
        contexts: ['page', 'link', 'selection']
    });
    
    chrome.contextMenus.create({
        id: 'saveLinkVaultLink',
        title: 'Save link to LinkVault',
        contexts: ['link']
    });
});

// Context menu click handler
chrome.contextMenus.onClicked.addListener((info, tab) => {
    let url = info.linkUrl || tab.url;
    let title = info.selectionText || tab.title;
    
    if (info.menuItemId === 'saveLinkVault' || info.menuItemId === 'saveLinkVaultLink') {
        // Store the context data
        const contextData = {
            url: url,
            title: title,
            fromContext: true,
            timestamp: Date.now()
        };
        
        chrome.storage.local.set({'contextData': contextData});
        
        // Open popup or send directly to app
        const linkVaultUrl = `https://linkvault.extension/add?` + 
            `url=${encodeURIComponent(url)}&` +
            `title=${encodeURIComponent(title)}&` +
            `source=context`;
        
        chrome.tabs.create({url: linkVaultUrl});
    }
});

// Command handler (keyboard shortcuts)
chrome.commands.onCommand.addListener((command) => {
    if (command === 'save-link') {
        chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
            const tab = tabs[0];
            const linkVaultUrl = `https://linkvault.extension/add?` + 
                `url=${encodeURIComponent(tab.url)}&` +
                `title=${encodeURIComponent(tab.title)}&` +
                `source=shortcut`;
            
            chrome.tabs.create({url: linkVaultUrl});
        });
    }
});

// Message handler from content script
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.action === 'quickSave') {
        const linkVaultUrl = `https://linkvault.extension/add?` + 
            `url=${encodeURIComponent(message.url)}&` +
            `title=${encodeURIComponent(message.title)}&` +
            `source=content`;
        
        chrome.tabs.create({url: linkVaultUrl});
        sendResponse({success: true});
    } else if (message.action === 'getSelection') {
        // Handle text selection for better context
        chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
            chrome.tabs.sendMessage(tabs[0].id, {action: 'getSelectedText'}, (response) => {
                sendResponse(response);
            });
        });
        return true; // Keep message channel open
    }
});

// Handle extension icon click
chrome.action.onClicked.addListener((tab) => {
    // This won't be called if popup is defined, but keeping for fallback
    const linkVaultUrl = `https://linkvault.extension/add?` + 
        `url=${encodeURIComponent(tab.url)}&` +
        `title=${encodeURIComponent(tab.title)}&` +
        `source=icon`;
    
    chrome.tabs.create({url: linkVaultUrl});
});

// Cleanup old data periodically
chrome.alarms.create('cleanup', {periodInMinutes: 60});

chrome.alarms.onAlarm.addListener((alarm) => {
    if (alarm.name === 'cleanup') {
        chrome.storage.local.get(null, (items) => {
            const now = Date.now();
            const toRemove = [];
            
            Object.keys(items).forEach(key => {
                if (key.startsWith('temp_') || key.startsWith('pending_')) {
                    const item = items[key];
                    if (item.timestamp && (now - item.timestamp) > 24 * 60 * 60 * 1000) { // 24 hours
                        toRemove.push(key);
                    }
                }
            });
            
            if (toRemove.length > 0) {
                chrome.storage.local.remove(toRemove);
                console.log('Cleaned up old data:', toRemove);
            }
        });
    }
});

// Handle tab updates to detect navigation
chrome.tabs.onUpdated.addListener((tabId, changeInfo, tab) => {
    if (changeInfo.status === 'complete' && tab.url) {
        // Could be used for auto-suggestions or page analysis
        // For now, just log for debugging
        console.log('Page loaded:', tab.url);
    }
});

// Badge text for notifications
function setBadgeText(text) {
    chrome.action.setBadgeText({text: text});
    chrome.action.setBadgeBackgroundColor({color: '#667eea'});
}

// Clear badge after some time
function clearBadge() {
    setTimeout(() => {
        chrome.action.setBadgeText({text: ''});
    }, 3000);
}

// Export functions for testing
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        setBadgeText,
        clearBadge
    };
}