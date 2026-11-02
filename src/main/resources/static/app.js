const $ = id => document.getElementById(id);
let revision = 0;
function text(tag, value, cls) { const el=document.createElement(tag); el.textContent=value; if(cls) el.className=cls; return el; }
async function library() {
  const current=++revision; $('results').replaceChildren(); $('status').textContent='Ready'; $('answer-intro').textContent='Matching passages will appear here.'; $('raw').textContent='Run a query to inspect the response.'; $('notice').textContent='';
  try { const res=await fetch(`/api/workspaces/${encodeURIComponent($('workspace').value)}/documents`); if(!res.ok) throw Error('Could not load collection.'); const docs=await res.json(); if(current!==revision)return;
    $('documents').replaceChildren(...docs.map(d=>{const card=text('article','', 'document');card.append(text('small',d.id),text('h3',d.title),text('p',d.text));return card;})); $('count').textContent=`${docs.length} documents`;
  } catch(e) { if(current===revision){$('notice').textContent=e.message;$('documents').replaceChildren();$('count').textContent='';} }
}
async function search() {
  const query=$('query').value.trim(); if(query.length<3){$('notice').textContent='Please enter at least three characters.';return;}
  const current=revision; $('search').disabled=true; $('status').textContent='Searching'; $('notice').textContent='';
  try { const res=await fetch(`/api/workspaces/${encodeURIComponent($('workspace').value)}/answers`,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({query,limit:3})}); const data=await res.json(); if(!res.ok)throw Error(data.detail||'Request failed.'); if(current!==revision)return;
    $('status').textContent=data.status==='supported'?'Evidence found':'Abstained'; $('answer-intro').textContent=data.status==='supported'?'Direct excerpts from matching documents. Scores measure query-term coverage, not factual confidence.':data.answer;
    $('results').replaceChildren(...data.evidence.map(e=>{const card=text('article','', 'evidence');card.append(text('span',`[${e.id}] · ${e.matchedTerms} matching terms · ${Math.round(e.coverage*100)}% coverage`,'tag'),text('h3',e.title),text('p',e.excerpt));return card;})); $('raw').textContent=JSON.stringify(data,null,2);
  } catch(e){if(current===revision){$('notice').textContent=e.message;$('status').textContent='Error';}}
  finally{$('search').disabled=false;}
}
$('workspace').addEventListener('change',library);$('search').addEventListener('click',search);
document.querySelectorAll('[data-query]').forEach(button=>button.addEventListener('click',()=>{$('query').value=button.dataset.query;search();}));
library();
