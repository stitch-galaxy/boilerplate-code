#import "CSGDesign.h"

@implementation CSGDesign

@synthesize palette;
@synthesize width;
@synthesize height;
@synthesize cells;
@synthesize backStitches;
@synthesize straightStitches;


-(id) initWithPalette: (CSGThreadsPalette*) aPalette Width: (uint32_t) aWidth Height: (uint32_t) aHeight Cells: (NSArray*) aCells BackStitches: (NSArray*) aBackStitches StraightStitches: (NSArray*) aStraightStitches
{
    if (self = [super init])
    {
        palette = aPalette;
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
    size_t size = palette.serializedLength;
    size += sizeof(uint32_t) * 2;
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
    [palette serializeWithBinaryEncoder:anEncoder];
    
    uint32_t *pWidth = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pWidth = width;
    uint32_t *pHeight = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pHeight = height;
    for(CSGDesignCell* cell in cells)
    {
        [cell serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    
    uint32_t *pBackStitchesNum = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pBackStitchesNum = (uint32_t) backStitches.count;
    for(CSGBackStitch* stitch in backStitches)
    {
        [stitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    
    uint32_t *pStraightStitchesNum = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pStraightStitchesNum = (uint32_t) straightStitches.count;
    for(CSGStraightStitch* stitch in straightStitches)
    {
        [stitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
}

- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder
{
    CSGThreadsPalette *aPalette = [[CSGThreadsPalette alloc] initWithBinaryDecoder:anDecoder];
    
    const uint32_t *pWidth = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aWidth = *pWidth;
    const uint32_t *pHeight = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aHeight = *pHeight;
    NSMutableArray *aCells = [[NSMutableArray alloc] initWithCapacity:aWidth * aHeight];
    for(uint32_t i = 0; i < aWidth * aHeight; ++i)
    {
        CSGDesignCell* cell = [[CSGDesignCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:aPalette];
        [aCells addObject:cell];
    }
    
    const uint32_t *pBackStitchesNum = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aBackStitchesNum = *pBackStitchesNum;
    NSMutableArray *aBackStitches = [[NSMutableArray alloc] initWithCapacity:aBackStitchesNum];
    for(uint32_t i = 0; i < aBackStitchesNum; ++i)
    {
        CSGBackStitch* stitch = [[CSGBackStitch alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:aPalette];
        [aBackStitches addObject:stitch];
    }
    
    const uint32_t *pStraightStitchesNum = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aStraightStitchesNum = *pStraightStitchesNum;
    NSMutableArray *aStraightStitches = [[NSMutableArray alloc] initWithCapacity:aStraightStitchesNum];
    for(uint32_t i = 0; i < aStraightStitchesNum; ++i)
    {
        CSGStraightStitch* stitch = [[CSGStraightStitch alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:aPalette];
        [aStraightStitches addObject:stitch];
    }
    
    return [self initWithPalette:aPalette Width:aWidth Height:aHeight Cells:aCells BackStitches:aBackStitches StraightStitches:aStraightStitches];
}

@end