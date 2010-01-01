
--
----  Custom Options Definition Table format
----
----  NOTES:
----  - using an enumerated table lets you specify the options order
----
----  These keywords must be lowercase for LuaParser to read them.
----
----  key:      the string used in the script.txt
----  name:     the displayed name
----  desc:     the description (could be used as a tooltip)
----  type:     the option type
----  def:      the default value;
----  min:      minimum value for number options
----  max:      maximum value for number options
----  step:     quantization step, aligned to the def value
----  maxlen:   the maximum string length for string options
----  items:    array of item strings for list options
----  scope:    'all', 'player', 'team', 'allyteam'      <<< not supported yet >>>
----
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
--

local options = {
	{
		type = "section",
		key = "debug",
		name = "Debug",
		desc = "Select Debug output",
	},
	{
		key="debuginfos",
		section = "debug",
		name="Debug Info",
		desc="Shows debug info from agai.info.*",
		type="string",
		def='IResource|AGUnit',
	},
	{
		key="debuglevel",
		section = "debug",
		name="Level of Debug",
		desc="",
		type="number",
		def=2,
		min=0,
		max=4,
		step=1.0,
	},
	{
		key="ignoredebugsettings",
                section = "debug",
                name="Ignore debug settings",
                desc="This settings is used to develop, the hard-coded values are used",
                type="bool",
                def=false,
        },

}

return options
