local infos = {
	{
		key    = 'shortName',
		value  = 'AGAI',
		desc   = 'machine conform name.',
	},
	{
		key    = 'version',
		value  = '0.1', -- AI version - !This comment is used for parsing!
	},
	{
		key    = 'className',
		value  = 'agai.loader.AGAIFactory',
		desc   = 'fully qualified name of a class that implements interface com.springrts.ai.AI',
	},
	{
		key    = 'name',
		value  = 'AGAI by http://abma.de/',
		desc   = 'human readable name.',
	},
	{
		key    = 'loadSupported',
		value  = 'no',
		desc   = 'whether this AI supports loading or not',
	},
	{
		key    = 'interfaceShortName',
		value  = 'Java', -- AI Interface name - !This comment is used for parsing!
		desc   = 'the shortName of the AI interface this AI needs',
	},
	{
		key    = 'interfaceVersion',
		value  = '0.1', -- AI Interface version - !This comment is used for parsing!
		desc   = 'the minimum version of the AI interface required by this AI',
	},
}

return infos