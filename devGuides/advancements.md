[List of Triggers](https://minecraft.wiki/w/Advancement_definition#List_of_triggers)

---

# Adding an Advancement

### <span style="color:pink">1.</span> Type `adv` for the code [snippet](resources/adv_snippet.jsonc)

### <span style="color:pink">2.</span> Fill in the details
(Navigate by pressing `tab`)<br>
The different frames are: `task`[], `goal`(), or `challenge`{}

### <span style="color:pink">3.</span> Create your criteria
\- Get your trigger from the [list](https://minecraft.wiki/w/Advancement_definition#List_of_triggers)<br>
\- Use your choice's associated `conditions`<br>
&nbsp;&nbsp;&nbsp;&nbsp;\- (Some don't have any, just delete the field)

### <span style="color:pink">4.</span> Add to the lang file
\- In the lang file, copy a pre-existing pair next to where the new one will go<br>
\- Select both keys, and change them to match the ones in the `advancement.json$display`<br>
&nbsp;&nbsp;&nbsp;&nbsp;\- (the tab should already match, but change it if needed)<br>
\- Change the actual title and description (duh)

### <span style="color:pink">5.</span> Write a readme entry
\- Copy the header up from the advancement below, and change it to the new title<br>
\- Write a short, one-line quip<br>
\- Actually explain the advancement

### <span style="color:pink">6.</span> MAKE A QUICK-LINK
<span style="color:red">DO NOT FORGET</span><br>
It's not at the top; the advancements area has its own [sub-section](../README.md#skip-to-advancement)
