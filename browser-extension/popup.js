document.addEventListener('DOMContentLoaded', function() {
    const titleInput = document.getElementById('title');
    const tagsInput = document.getElementById('tags');
    const notesInput = document.getElementById('notes');
    const saveBtn = document.getElementById('saveBtn');
    const statusDiv = document.getElementById('status');
    const urlDiv = document.getElementById('currentUrl');
    const openAppLink = document.getElementById('openApp');
    const tagSuggestions = document.getElementById('tagSuggestions');
    
    let currentTab = null;
    
    // Common tag suggestions
    const commonTags = [
        'work', 'personal', 'important', 'read-later', 'reference',
        'tutorial', 'documentation', 'news', 'shopping', 'entertainment',
        'social', 'tech', 'design', 'development', 'research'
    ];
    
    // Get current tab info
    chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
        currentTab = tabs[0];
        urlDiv.textContent = currentTab.url;
        titleInput.value = currentTab.title || '';
        titleInput.focus();
        titleInput.select();
    });
    
    // Tag suggestions
    tagsInput.addEventListener('input', function() {
        const value = this.value.toLowerCase();
        const lastTag = value.split(',').pop().trim();
        
        if (lastTag.length > 0) {
            const suggestions = commonTags.filter(tag => 
                tag.toLowerCase().includes(lastTag) && 
                !value.includes(tag)
            );
            
            if (suggestions.length > 0) {
                tagSuggestions.innerHTML = suggestions
                    .map(tag => `<div class="tag-suggestion" data-tag="${tag}">${tag}</div>`)
                    .join('');
                tagSuggestions.style.display = 'block';
            } else {
                tagSuggestions.style.display = 'none';
            }
        } else {
            tagSuggestions.style.display = 'none';
        }
    });
    
    // Handle tag suggestion clicks
    tagSuggestions.addEventListener('click', function(e) {
        if (e.target.classList.contains('tag-suggestion')) {
            const tag = e.target.dataset.tag;
            const currentTags = tagsInput.value.split(',').map(t => t.trim());
            currentTags[currentTags.length - 1] = tag;
            tagsInput.value = currentTags.join(', ') + ', ';
            tagSuggestions.style.display = 'none';
            tagsInput.focus();
        }
    });
    
    // Hide suggestions when clicking outside
    document.addEventListener('click', function(e) {
        if (!tagsInput.contains(e.target) && !tagSuggestions.contains(e.target)) {
            tagSuggestions.style.display = 'none';
        }
    });
    
    // Save button click
    saveBtn.addEventListener('click', function() {
        if (!currentTab) return;
        
        const linkData = {
            url: currentTab.url,
            title: titleInput.value.trim() || currentTab.title,
            tags: tagsInput.value.trim(),
            notes: notesInput.value.trim(),
            timestamp: Date.now()
        };
        
        // Validate URL
        if (!linkData.url || (!linkData.url.startsWith('http://') && !linkData.url.startsWith('https://'))) {
            showStatus('Please navigate to a valid webpage first.', 'error');
            return;
        }
        
        // Show loading state
        saveBtn.disabled = true;
        saveBtn.innerHTML = '<span class="loading"></span>Saving...';
        
        // Send to LinkVault app
        const linkVaultUrl = `https://linkvault.extension/add?` + 
            `url=${encodeURIComponent(linkData.url)}&` +
            `title=${encodeURIComponent(linkData.title)}&` +
            `tags=${encodeURIComponent(linkData.tags)}&` +
            `notes=${encodeURIComponent(linkData.notes)}`;
        
        // Try to open the LinkVault app
        chrome.tabs.create({url: linkVaultUrl}, function(tab) {
            // Store the link data in case the app needs it
            chrome.storage.local.set({
                'pendingLink': linkData,
                'linkSavedAt': Date.now()
            });
            
            showStatus('Link sent to LinkVault!', 'success');
            
            // Reset form
            setTimeout(() => {
                titleInput.value = '';
                tagsInput.value = '';
                notesInput.value = '';
                saveBtn.disabled = false;
                saveBtn.innerHTML = 'Save to LinkVault';
                window.close();
            }, 1500);
        });
    });
    
    // Open app link
    openAppLink.addEventListener('click', function(e) {
        e.preventDefault();
        chrome.tabs.create({url: 'https://linkvault.extension/'});
        window.close();
    });
    
    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        if (e.ctrlKey && e.key === 'Enter') {
            saveBtn.click();
        } else if (e.key === 'Escape') {
            window.close();
        }
    });
    
    function showStatus(message, type) {
        statusDiv.innerHTML = `<div class="status ${type}">${message}</div>`;
        setTimeout(() => {
            statusDiv.innerHTML = '';
        }, 3000);
    }
    
    // Load saved tags for suggestions
    chrome.storage.local.get(['savedTags'], function(result) {
        if (result.savedTags) {
            commonTags.push(...result.savedTags.filter(tag => !commonTags.includes(tag)));
        }
    });
});