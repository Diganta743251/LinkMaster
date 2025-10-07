// Content script for LinkVault extension
(function() {
    'use strict';
    
    // Avoid multiple injections
    if (window.linkVaultExtensionLoaded) {
        return;
    }
    window.linkVaultExtensionLoaded = true;
    
    console.log('LinkVault content script loaded');
    
    // Global variables
    let selectedText = '';
    let selectedLinks = [];
    
    // Track text selection
    document.addEventListener('selectionchange', function() {
        const selection = window.getSelection();
        selectedText = selection.toString().trim();
        
        // Extract links from selection
        if (selectedText) {
            selectedLinks = extractLinksFromText(selectedText);
        } else {
            selectedLinks = [];
        }
    });
    
    // Extract URLs from text
    function extractLinksFromText(text) {
        const urlRegex = /(https?:\/\/[^\s]+)/g;
        const matches = text.match(urlRegex) || [];
        return matches.map(url => ({
            url: url.replace(/[.,;!?]$/, ''), // Remove trailing punctuation
            title: getContextAroundUrl(text, url)
        }));
    }
    
    // Get context around URL for better title
    function getContextAroundUrl(text, url) {
        const index = text.indexOf(url);
        const start = Math.max(0, index - 50);
        const end = Math.min(text.length, index + url.length + 50);
        return text.substring(start, end).trim();
    }
    
    // Keyboard shortcut handler
    document.addEventListener('keydown', function(e) {
        // Ctrl+Shift+L (or Cmd+Shift+L on Mac)
        if ((e.ctrlKey || e.metaKey) && e.shiftKey && e.key === 'L') {
            e.preventDefault();
            
            // If there's selected text with links, use the first link
            if (selectedLinks.length > 0) {
                chrome.runtime.sendMessage({
                    action: 'quickSave',
                    url: selectedLinks[0].url,
                    title: selectedLinks[0].title || document.title,
                    selectedText: selectedText
                });
            } else {
                // Save current page
                chrome.runtime.sendMessage({
                    action: 'quickSave',
                    url: window.location.href,
                    title: document.title,
                    selectedText: selectedText
                });
            }
            
            // Show temporary notification
            showNotification('Saving to LinkVault...', 'info');
        }
    });
    
    // Context menu enhancement
    document.addEventListener('contextmenu', function(e) {
        const selection = window.getSelection().toString();
        if (selection) {
            // Store selection data for context menu
            chrome.runtime.sendMessage({
                action: 'setContextData',
                selection: selection,
                url: window.location.href,
                links: selectedLinks
            });
        }
    });
    
    // Message handler from background script
    chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
        if (message.action === 'getSelectedText') {
            sendResponse({
                selectedText: selectedText,
                selectedLinks: selectedLinks,
                pageTitle: document.title,
                pageUrl: window.location.href
            });
        } else if (message.action === 'highlightSavedLink') {
            // Highlight links that have been saved (future feature)
            highlightSavedLinks(message.savedUrls);
        }
    });
    
    // Show temporary notification
    function showNotification(message, type = 'info') {
        // Remove existing notification
        const existing = document.getElementById('linkvault-notification');
        if (existing) {
            existing.remove();
        }
        
        // Create notification element
        const notification = document.createElement('div');
        notification.id = 'linkvault-notification';
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: ${type === 'success' ? '#28a745' : type === 'error' ? '#dc3545' : '#667eea'};
            color: white;
            padding: 12px 20px;
            border-radius: 6px;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            font-size: 14px;
            font-weight: 500;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 10000;
            animation: slideIn 0.3s ease-out;
        `;
        
        // Add animation styles
        const style = document.createElement('style');
        style.textContent = `
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
        
        notification.textContent = message;
        document.body.appendChild(notification);
        
        // Auto-remove after 3 seconds
        setTimeout(() => {
            notification.style.animation = 'slideOut 0.3s ease-in';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
                if (style.parentNode) {
                    style.parentNode.removeChild(style);
                }
            }, 300);
        }, 3000);
    }
    
    // Highlight saved links (future feature)
    function highlightSavedLinks(savedUrls) {
        if (!savedUrls || savedUrls.length === 0) return;
        
        const links = document.querySelectorAll('a[href]');
        links.forEach(link => {
            const href = link.href;
            if (savedUrls.includes(href)) {
                link.style.cssText += `
                    border-left: 3px solid #667eea !important;
                    padding-left: 4px !important;
                    background: rgba(102, 126, 234, 0.1) !important;
                `;
                link.title = (link.title ? link.title + ' | ' : '') + 'Saved in LinkVault';
            }
        });
    }
    
    // Auto-detect and suggest saving interesting links
    function detectInterestingContent() {
        // Look for article content, documentation, tutorials, etc.
        const indicators = [
            'article', 'main', '[role="main"]', '.content', '.post',
            '.tutorial', '.documentation', '.guide', '.readme'
        ];
        
        for (const selector of indicators) {
            const element = document.querySelector(selector);
            if (element && element.textContent.length > 1000) {
                // This looks like substantial content
                return true;
            }
        }
        
        return false;
    }
    
    // Initialize content detection
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            setTimeout(() => {
                if (detectInterestingContent()) {
                    console.log('LinkVault: Detected interesting content on this page');
                    // Could show a subtle suggestion to save
                }
            }, 2000);
        });
    } else {
        setTimeout(() => {
            if (detectInterestingContent()) {
                console.log('LinkVault: Detected interesting content on this page');
            }
        }, 2000);
    }
    
    // Page visibility change handler
    document.addEventListener('visibilitychange', function() {
        if (document.hidden) {
            // Page is being hidden, could track reading time
            console.log('LinkVault: Page hidden');
        } else {
            // Page is visible again
            console.log('LinkVault: Page visible');
        }
    });
    
})();