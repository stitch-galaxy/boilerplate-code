#import "CSGDesign.h"

@implementation CSGDesign

@synthesize width;
@synthesize height;
@synthesize cells;
@synthesize backStitches;
@synthesize straightStitches;

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}

-(id) initWithWidth: (uint32_t) aWidth Height: (uint32_t) aHeight Cells: (NSArray*) aCells BackStitches: (NSArray*) aBackStitches StraightStitches: (NSArray*) aStraightStitches
{
    if (self = [super init])
    {
        width = aWidth;
        height = aHeight;
        cells = aCells;
        backStitches = aBackStitches;
        straightStitches = aStraightStitches;
    }
    return self;
}

@end

@implementation CSGDesign (Serialization)

- (size_t) serializedLength
{
    size_t size = sizeof(uint32_t) * 2;
    for(CSGDesignCell* cell in cells)
    {
        size += cell.serializedLength;
    }
    size += sizeof(uint32_t);
    for(CSGBackStitch* stitch in backStitches)
    {
        size += stitch.serializedLength;
    }
    size += sizeof(uint32_t);
    for(CSGStraightStitch* stitch in straightStitches)
    {
        size += stitch.serializedLength;
    }
    return size;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
{
    uint32_t *pWidth = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pWidth = width;
    uint32_t *pHeight = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pHeight = height;
    for(CSGDesignCell* cell in cells)
    {
        [cell serializeWithBinaryEncoder:anEncoder];
    }
    
    uint32_t *pBackStitchesNum = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pBackStitchesNum = (uint32_t) backStitches.count;
    for(CSGBackStitch* stitch in backStitches)
    {
        [stitch serializeWithBinaryEncoder:anEncoder];
    }
    
    uint32_t *pStraightStitchesNum = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pStraightStitchesNum = (uint32_t) straightStitches.count;
    for(CSGStraightStitch* stitch in straightStitches)
    {
        [stitch serializeWithBinaryEncoder:anEncoder];
    }
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    const uint32_t *pWidth = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aWidth = *pWidth;
    const uint32_t *pHeight = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aHeight = *pHeight;
    NSMutableArray *aCells = [[NSMutableArray alloc] initWithCapacity:aWidth * aHeight];
    for(uint32_t i = 0; i < aWidth * aHeight; ++i)
    {
        CSGDesignCell* cell = [CSGDesignCell deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
        [aCells addObject:cell];
    }
    
    const uint32_t *pBackStitchesNum = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aBackStitchesNum = *pBackStitchesNum;
    NSMutableArray *aBackStitches = [[NSMutableArray alloc] initWithCapacity:aBackStitchesNum];
    for(uint32_t i = 0; i < aBackStitchesNum; ++i)
    {
        CSGBackStitch* stitch = [CSGBackStitch deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
        [aBackStitches addObject:stitch];
    }
    
    const uint32_t *pStraightStitchesNum = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aStraightStitchesNum = *pStraightStitchesNum;
    NSMutableArray *aStraightStitches = [[NSMutableArray alloc] initWithCapacity:aStraightStitchesNum];
    for(uint32_t i = 0; i < aStraightStitchesNum; ++i)
    {
        CSGStraightStitch* stitch = [CSGStraightStitch deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
        [aStraightStitches addObject:stitch];
    }
    
    CSGDesign* aDesign = [[CSGDesign alloc] initWithWidth:aWidth Height:aHeight Cells:aCells BackStitches:aBackStitches StraightStitches:aStraightStitches];
    return [registry getDesign: aDesign];
}

@end