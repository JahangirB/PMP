/**
 * PMP Prep cloud sync. Keeps your study progress and notes in one file in your Google Drive,
 * "PMP Prep/pmp-sync.json", so the web, Android and iPhone apps can share it.
 *
 * Deploy: Deploy → New deployment → Web app → Execute as: Me, Who has access: Anyone.
 * "Anyone" only means the URL answers without a Google sign-in. Every request must carry
 * SYNC_CODE below, so keep the URL and the code private. To revoke access, change the
 * code and deploy a new version.
 *
 * This script reads and writes only that one file. It sends nothing anywhere else.
 */
const SYNC_CODE = '__SYNC_CODE__';
const FOLDER_NAME = 'PMP Prep';
const FILE_NAME = 'pmp-sync.json';

function doPost(e) {
  let req;
  try { req = JSON.parse(e.postData.contents); } catch (err) { return reply({ ok: false, error: 'bad-request' }); }
  if (!req || req.code !== SYNC_CODE || SYNC_CODE.indexOf('__') === 0) return reply({ ok: false, error: 'bad-code' });

  const lock = LockService.getScriptLock();
  if (!lock.tryLock(20000)) return reply({ ok: false, error: 'busy' });
  try {
    const props = PropertiesService.getScriptProperties();
    const rev = Number(props.getProperty('rev') || 0);
    const file = findFile();
    if (req.op === 'ping') return reply({ ok: true, rev: rev, savedAt: Number(props.getProperty('savedAt') || 0) });
    if (req.op === 'get') return reply({ ok: true, rev: rev, doc: file ? JSON.parse(file.getBlob().getDataAsString()) : null });
    if (req.op === 'put') {
      if (Number(req.baseRev) !== rev) {
        return reply({ ok: false, conflict: true, rev: rev, doc: file ? JSON.parse(file.getBlob().getDataAsString()) : null });
      }
      if (!req.doc || req.doc.type !== 'pmp-progress') return reply({ ok: false, error: 'bad-doc' });
      const text = JSON.stringify(req.doc);
      if (file) file.setContent(text); else folder().createFile(FILE_NAME, text, 'application/json');
      props.setProperties({ rev: String(rev + 1), savedAt: String(Date.now()) });
      return reply({ ok: true, rev: rev + 1 });
    }
    return reply({ ok: false, error: 'bad-op' });
  } finally {
    lock.releaseLock();
  }
}

/** Opening the URL in a browser shows this, so you can check the deployment works. */
function doGet() {
  return reply({ ok: true, app: 'PMP Prep sync', note: 'Working. The apps talk to this URL with your sync code.' });
}

function folder() {
  const it = DriveApp.getFoldersByName(FOLDER_NAME);
  return it.hasNext() ? it.next() : DriveApp.createFolder(FOLDER_NAME);
}

function findFile() {
  const id = PropertiesService.getScriptProperties().getProperty('fileId');
  if (id) { try { const f = DriveApp.getFileById(id); if (!f.isTrashed()) return f; } catch (err) {} }
  const it = folder().getFilesByName(FILE_NAME);
  if (!it.hasNext()) return null;
  const f = it.next();
  PropertiesService.getScriptProperties().setProperty('fileId', f.getId());
  return f;
}

function reply(obj) {
  return ContentService.createTextOutput(JSON.stringify(obj)).setMimeType(ContentService.MimeType.JSON);
}
