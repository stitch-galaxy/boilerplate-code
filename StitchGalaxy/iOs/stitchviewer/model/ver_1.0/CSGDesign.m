#import "CSGDesign.h"

@implementation CSGDesign
{
    uint8_t *done;
}

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

-(id) initWithWidth: (uint32_t) aWidth Height: (uint32_t) aHeight Cells: (NSArray*) aCells BackStitches: (NSArray*) aBackStitches StraightStitches: (NSArray*) aStraightStitches Done: (uint8_t*) aDone
{
    if (self = [super init])
    {
        width = aWidth;
        height = aHeight;
        cells = aCells;
        backStitches = aBackStitches;
        straightStitches = aStraightStitches;
        done = aDone;
    }
    return self;
}

- (void*) doneBitMask
{
    return done;
}

- (void) dealloc
{
    free(done);
    // [super dealloc]; << provided by the compiler
}

- (bool) isDoneAtX: (uint32_t) x Y: (uint32_t) y
{
    uint32_t idx = (width * y + x);
    return [self isDoneImpl: idx];
}

- (bool) isDoneImpl: (uint32_t) idx
{
    uint8_t *buf = done + idx / 8;
    uint8_t targetByte = *buf;
    
    uint8_t inByteIdx = idx % 8;
    uint8_t value = targetByte >> (8 - inByteIdx - 1);
    return value & 0x1;
}

- (void) setDone: (bool) isDone AtX: (uint32_t) x Y: (uint32_t) y
{
    uint32_t idx = (width * y + x);
    [self setDoneImpl:isDone Idx:idx];
}

- (void) setDoneImpl: (bool) isDone Idx: (uint32_t) idx
{
    uint8_t *buf = done + idx / 8;
    uint8_t sourceByte = *buf;
    
    uint8_t inByteIdx = idx % 8;
    uint8_t shift = 8 - inByteIdx - 1;
    
    uint8_t dstByte = sourceByte;
    if (isDone)
    {
        dstByte |= 1 << shift;
    }
    else
    {
        dstByte &= ~(1 << shift);
    }
    *buf = dstByte;
}

- (void) setDone: (bool) isDone FromX: (uint32_t) fromX Y: (uint32_t) fromY ToX:(uint32_t) toX Y: (uint32_t) toY
{
    for(uint32_t x = fromX; x <= toX; ++x)
    {
        for(uint32_t y = fromY; y <= toY; ++y)
        {
            [self setDone:isDone AtX:x Y:y];
        }
    }
}

- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGDesign: object];
}

- (BOOL) isEqualToCSGDesign: (CSGDesign*) aDesign
{
    if (self == aDesign)
    {
        return YES;
    }
    if (width != aDesign.width)
    {
        return NO;
    }
    if (height != aDesign.height)
    {
        return NO;
    }
    for(uint32_t i = 0; i < width * height; ++i)
    {
        if (![[cells objectAtIndex:i] isEqual:[aDesign.cells objectAtIndex:i]])
        {
            return NO;
        }
    }
    
    if (memcmp(done, aDesign.doneBitMask, (width * height - 1) / 8 + 1) != 0)
    {
        return NO;
    }
    
    if (backStitches.count != aDesign.backStitches.count)
    {
        return NO;
    }
    
    for(uint32_t i = 0; i < backStitches.count; ++i)
    {
        if (![[backStitches objectAtIndex:i] isEqual:[aDesign.backStitches objectAtIndex:i]])
        {
            return NO;
        }
    }
    
    if (straightStitches.count != aDesign.straightStitches.count)
    {
        return NO;
    }
    
    for(uint32_t i = 0; i < straightStitches.count; ++i)
    {
        if (![[straightStitches objectAtIndex:i] isEqual:[aDesign.straightStitches objectAtIndex:i]])
        {
            return NO;
        }
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash * 31 + width;
    hash = hash * 31 + height;
    for(uint32_t i = 0; i < width * height; ++i)
    {
        CSGDesignCell* o = [cells objectAtIndex:i];
        hash = hash * 31 + o.hash;
    }
    
    for(uint32_t i = 0; i < (width * height - 1) / 8 + 1; ++i)
    {
        hash = hash * 31 + *(done + i);
    }
    
    
    for(uint32_t i = 0; i < backStitches.count; ++i)
    {
        CSGBackStitch* o = [backStitches objectAtIndex:i];
        hash = hash * 31 + o.hash;
    }
    for(uint32_t i = 0; i < straightStitches.count; ++i)
    {
        CSGStraightStitch* o = [straightStitches objectAtIndex:i];
        hash = hash * 31 + o.hash;
    }
    
    return hash;
}


@end