# LinkVault Browser Extension

A powerful browser extension that allows you to save links directly to your LinkVault Android app with one click.

## Features

- **One-Click Saving**: Save any webpage to LinkVault with a single click
- **Smart Form**: Auto-fills title and provides tag suggestions
- **Keyboard Shortcuts**: Use Ctrl+Shift+L (Cmd+Shift+L on Mac) to quickly save
- **Context Menu**: Right-click on any link or page to save
- **Text Selection**: Select text containing URLs to save specific links
- **Tag Suggestions**: Common tags and previously used tags for easy organization
- **Offline Storage**: Temporarily stores data if the app isn't immediately available

## Installation

### Chrome/Chromium/Edge

1. Download or clone this extension folder
2. Open Chrome/Edge and go to `chrome://extensions/` or `edge://extensions/`
3. Enable "Developer mode" in the top right
4. Click "Load unpacked" and select the `browser-extension` folder
5. The LinkVault extension should now appear in your extensions

### Firefox

1. Download or clone this extension folder
2. Open Firefox and go to `about:debugging`
3. Click "This Firefox"
4. Click "Load Temporary Add-on"
5. Select the `manifest.json` file from the extension folder

## Usage

### Saving Links

1. **Extension Popup**: Click the LinkVault icon in your browser toolbar
2. **Keyboard Shortcut**: Press Ctrl+Shift+L (Cmd+Shift+L on Mac)
3. **Context Menu**: Right-click on any page or link and select "Save to LinkVault"
4. **Text Selection**: Select text containing URLs and use the context menu

### Form Fields

- **Title**: Auto-filled with page title, editable
- **Tags**: Add comma-separated tags with auto-suggestions
- **Notes**: Optional notes about the link

### Tag Suggestions

The extension provides common tag suggestions:
- work, personal, important, read-later, reference
- tutorial, documentation, news, shopping, entertainment
- social, tech, design, development, research

## How It Works

1. When you save a link, the extension opens a special URL in a new tab
2. This URL contains the link data as parameters
3. Your LinkVault Android app intercepts this URL and adds the link
4. The browser tab closes automatically after the link is saved

## LinkVault App Integration

Make sure your LinkVault Android app is installed and configured to handle:
- Custom URL scheme: `lv://`
- Extension URLs: `https://linkvault.extension/add`

## Keyboard Shortcuts

- **Ctrl+Shift+L** (Cmd+Shift+L on Mac): Quick save current page
- **Ctrl+Enter**: Save link from popup form
- **Escape**: Close popup

## Privacy

- No data is sent to external servers
- All communication is between the extension and your local LinkVault app
- Temporary data is automatically cleaned up after 24 hours

## Troubleshooting

### Extension Not Working
1. Make sure LinkVault app is installed on your Android device
2. Check that the app is set as default for `lv://` URLs
3. Verify the extension has necessary permissions

### Links Not Saving
1. Ensure you're on a valid webpage (http:// or https://)
2. Check that the LinkVault app is running
3. Try refreshing the page and saving again

### Popup Not Opening
1. Check if the extension is enabled in browser settings
2. Try reloading the extension
3. Restart your browser

## Development

### File Structure
```
browser-extension/
├── manifest.json       # Extension configuration
├── popup.html         # Extension popup interface
├── popup.js           # Popup functionality
├── background.js      # Background service worker
├── content.js         # Content script for page interaction
├── icons/             # Extension icons (16, 32, 48, 128px)
└── README.md          # This file
```

### Building
No build process required - this is a vanilla JavaScript extension.

### Testing
1. Load the extension in developer mode
2. Test on various websites
3. Check browser console for any errors
4. Verify integration with LinkVault app

## Version History

### v1.0.0
- Initial release
- Basic link saving functionality
- Popup interface with form
- Keyboard shortcuts
- Context menu integration
- Tag suggestions
- Content script for text selection

## Support

For issues related to:
- **Extension functionality**: Check browser console for errors
- **App integration**: Ensure LinkVault app is properly configured
- **Feature requests**: Consider contributing to the project

## License

This extension is part of the LinkVault project and follows the same license terms.