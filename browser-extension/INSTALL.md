# LinkVault Browser Extension Installation Guide

## Quick Installation

### For Chrome, Edge, Brave, and other Chromium browsers:

1. **Download the Extension**
   - Download the `browser-extension` folder from the LinkVault project
   - Or clone the repository: `git clone [repository-url]`

2. **Open Extension Management**
   - Chrome: Go to `chrome://extensions/`
   - Edge: Go to `edge://extensions/`
   - Brave: Go to `brave://extensions/`

3. **Enable Developer Mode**
   - Toggle the "Developer mode" switch in the top right corner

4. **Load the Extension**
   - Click "Load unpacked" button
   - Navigate to and select the `browser-extension` folder
   - Click "Select Folder"

5. **Verify Installation**
   - You should see the LinkVault extension in your extensions list
   - The LinkVault icon should appear in your browser toolbar

### For Firefox:

1. **Download the Extension**
   - Download the `browser-extension` folder

2. **Open Debug Page**
   - Go to `about:debugging` in Firefox
   - Click "This Firefox" in the left sidebar

3. **Load Temporary Add-on**
   - Click "Load Temporary Add-on..."
   - Navigate to the `browser-extension` folder
   - Select the `manifest.json` file
   - Click "Open"

4. **Note for Firefox**
   - This loads the extension temporarily
   - It will be removed when Firefox restarts
   - For permanent installation, the extension needs to be signed by Mozilla

## Post-Installation Setup

### 1. Configure LinkVault App
Make sure your LinkVault Android app is installed and configured:
- Install LinkVault app on your Android device
- Open the app at least once to initialize
- Enable the app to handle custom URLs in Android settings

### 2. Test the Extension
1. Click the LinkVault icon in your browser toolbar
2. You should see the LinkVault popup
3. Try saving a link to test the integration

### 3. Set Up Permissions (if prompted)
The extension may request permissions for:
- **Active Tab**: To read the current page URL and title
- **Storage**: To save temporary data and preferences
- **Context Menus**: To add right-click menu options

## Troubleshooting

### Extension Not Appearing
- Refresh the extensions page
- Check if Developer mode is enabled
- Try restarting your browser

### Popup Not Working
- Check browser console for errors (F12 → Console)
- Verify all extension files are present
- Try reloading the extension

### App Integration Issues
- Ensure LinkVault app is installed on your device
- Check that the app can handle `lv://` URLs
- Try opening `lv://test` in your browser to test URL handling

### Permission Errors
- Grant all requested permissions
- Check browser security settings
- Try disabling other extensions temporarily

## Advanced Configuration

### Custom URL Scheme
If you need to modify the URL scheme:
1. Edit `background.js` and `popup.js`
2. Change `https://linkvault.extension/` to your preferred scheme
3. Update your LinkVault app to handle the new scheme

### Tag Suggestions
To add custom tag suggestions:
1. Edit `popup.js`
2. Modify the `commonTags` array
3. Reload the extension

## Updating the Extension

### Manual Update
1. Download the latest version
2. Go to extensions page
3. Click "Reload" on the LinkVault extension
4. Or remove and re-add the extension

### Development Updates
If you're developing:
1. Make changes to the extension files
2. Go to extensions page
3. Click the reload icon on LinkVault extension
4. Test your changes

## Security Notes

- The extension only communicates with your local LinkVault app
- No data is sent to external servers
- All permissions are used only for core functionality
- Temporary data is automatically cleaned up

## Getting Help

### Common Issues
1. **"Extension failed to load"**: Check file permissions and folder structure
2. **"Popup won't open"**: Verify manifest.json is valid
3. **"App not responding"**: Ensure LinkVault app is running and configured

### Debug Information
To get debug information:
1. Open browser developer tools (F12)
2. Go to Console tab
3. Look for LinkVault-related messages
4. Check for any error messages

### Support Channels
- Check the main LinkVault app documentation
- Review browser extension logs
- Verify Android app is properly configured

## Uninstalling

### Chrome/Edge/Brave
1. Go to extensions page
2. Find LinkVault extension
3. Click "Remove"
4. Confirm removal

### Firefox
1. Go to `about:addons`
2. Find LinkVault in Extensions
3. Click "Remove"
4. Confirm removal

The extension will be completely removed and won't leave any traces in your browser.